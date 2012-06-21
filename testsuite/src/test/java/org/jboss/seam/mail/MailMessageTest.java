/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.mail;

import java.io.IOException;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.attachments.URLAttachment;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.InvalidAddressException;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.SendFailedException;
import org.jboss.seam.mail.core.enumerations.ContentDisposition;
import org.jboss.seam.mail.core.enumerations.MessagePriority;
import org.jboss.seam.mail.util.Deployments;
import org.jboss.seam.mail.util.MailTestUtil;
import org.jboss.seam.mail.util.MailUtility;
import org.jboss.seam.mail.util.MessageConverter;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.solder.resourceLoader.ResourceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.subethamail.wiser.Wiser;

/**
 * @author Cody Lerum
 */
@RunWith(Arquillian.class)
public class MailMessageTest {
    @Deployment(name = "mailMessage")
    public static Archive<?> createTestArchive() {
        return Deployments.baseDeployment().addAsResource("template.text.velocity")
                .addPackages(true, MailMessageTest.class.getPackage());
    }

    @Inject
    private Instance<MailMessage> mailMessage;

    @Inject
    private Instance<Session> session;

    @Inject
    private ResourceProvider resourceProvider;

    @Inject
    private MailConfig mailConfig;

    @Inject
    private Person person;

    String fromName = "Seam Framework";
    String fromAddress = "seam@jboss.org";
    String replyToName = "No Reply";
    String replyToAddress = "no-reply@seam-mal.test";
    String toName = "Seamy Seamerson";
    String toAddress = "seamy.seamerson@seam-mail.test";
    String ccName = "Red Hatty";
    String ccAddress = "red.hatty@jboss.org";

    String htmlBody = "<html><body><b>Hello</b> World!</body></html>";
    String textBody = "This is a Text Body!";

    @Test
    public void testTextMailMessage() throws MessagingException, IOException {

        String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();
        String messageId = "1234@seam.test.com";

        EmailMessage e;

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            e = mailMessage.get().from(MailTestUtil.getAddressHeader(fromName, fromAddress)).replyTo(replyToAddress)
                    .to(MailTestUtil.getAddressHeader(toName, toAddress)).subject(subject).bodyText(textBody)
                    .importance(MessagePriority.HIGH).messageId(messageId).send(session.get());
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser
                .getMessages().size() == 1);

        MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals(MailTestUtil.getAddressHeader(fromName, fromAddress), mess.getHeader("From", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(replyToAddress), mess.getHeader("Reply-To", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(toName, toAddress), mess.getHeader("To", null));
        Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
        Assert.assertEquals(MessagePriority.HIGH.getPriority(), mess.getHeader("Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getX_priority(), mess.getHeader("X-Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getImportance(), mess.getHeader("Importance", null));
        Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));
        Assert.assertEquals(messageId, MailUtility.headerStripper(mess.getHeader("Message-ID", null)));

        MimeMultipart mixed = (MimeMultipart) mess.getContent();
        BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(textBody, MailTestUtil.getStringContent(text));
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testTextMailMessageSpecialCharacters() throws MessagingException, IOException {

        String subject = "Sometimes subjects have speical characters like ü - " + java.util.UUID.randomUUID().toString();
        String specialTextBody = "This is a Text Body with a special character - ü";

        EmailMessage e;

        String messageId = "1234@seam.test.com";

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            e = mailMessage.get().from(MailTestUtil.getAddressHeader(fromName, fromAddress)).replyTo(replyToAddress)
                    .to(MailTestUtil.getAddressHeader(toName, toAddress)).subject(subject).bodyText(specialTextBody)
                    .importance(MessagePriority.HIGH).messageId(messageId).send(session.get());
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser
                .getMessages().size() == 1);

        MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals("Subject has been modified", subject,
                MimeUtility.decodeText(MimeUtility.unfold(mess.getHeader("Subject", null))));

        MimeMultipart mixed = (MimeMultipart) mess.getContent();
        BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(specialTextBody, MimeUtility.decodeText(MailTestUtil.getStringContent(text)));
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testHTMLMailMessage() throws MessagingException, IOException {
        String subject = "HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

        EmailMessage e;

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            e = mailMessage
                    .get()
                    .from(MailTestUtil.getAddressHeader(fromName, fromAddress))
                    .replyTo(MailTestUtil.getAddressHeader(replyToName, replyToAddress))
                    .to(person)
                    .subject(subject)
                    .bodyHtml(htmlBody)
                    .importance(MessagePriority.HIGH)
                    .addAttachment(
                            new URLAttachment("http://design.jboss.org/seam/logo/final/seam_mail_85px.png", "seamLogo.png",
                                    ContentDisposition.INLINE)).send(session.get());
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser
                .getMessages().size() == 1);

        MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals(MailTestUtil.getAddressHeader(fromName, fromAddress), mess.getHeader("From", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(replyToName, replyToAddress), mess.getHeader("Reply-To", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(toName, toAddress), mess.getHeader("To", null));
        Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
        Assert.assertEquals(MessagePriority.HIGH.getPriority(), mess.getHeader("Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getX_priority(), mess.getHeader("X-Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getImportance(), mess.getHeader("Importance", null));
        Assert.assertEquals(e.getLastMessageId(), MailUtility.headerStripper(mess.getHeader("Message-ID", null)));
        Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));

        MimeMultipart mixed = (MimeMultipart) mess.getContent();
        MimeMultipart related = (MimeMultipart) mixed.getBodyPart(0).getContent();
        BodyPart html = related.getBodyPart(0);
        BodyPart attachment1 = related.getBodyPart(1);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue(related.getContentType().startsWith("multipart/related"));
        Assert.assertEquals(2, related.getCount());

        Assert.assertTrue(html.getContentType().startsWith("text/html"));
        Assert.assertEquals(htmlBody, MailTestUtil.getStringContent(html));

        Assert.assertTrue(attachment1.getContentType().startsWith("image/png;"));
        Assert.assertEquals("seamLogo.png", attachment1.getFileName());
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testHTMLTextAltMailMessage() throws MessagingException, IOException {
        String subject = "HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            mailMessage
                    .get()
                    .from(MailTestUtil.getAddressHeader(fromName, fromAddress))
                    .to(person)
                    .subject(subject)
                    .bodyHtmlTextAlt(htmlBody, textBody)
                    .importance(MessagePriority.LOW)
                    .deliveryReceipt(fromAddress)
                    .readReceipt("seam.test")
                    .addAttachment("template.text.velocity", "text/plain", ContentDisposition.ATTACHMENT,
                            resourceProvider.loadResourceStream("template.text.velocity"))
                    .addAttachment(
                            new URLAttachment("http://design.jboss.org/seam/logo/final/seam_mail_85px.png", "seamLogo.png",
                                    ContentDisposition.INLINE)).send(session.get());
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser
                .getMessages().size() == 1);

        MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals(MailTestUtil.getAddressHeader(fromName, fromAddress), mess.getHeader("From", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(toName, toAddress), mess.getHeader("To", null));
        Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
        Assert.assertEquals(MessagePriority.LOW.getPriority(), mess.getHeader("Priority", null));
        Assert.assertEquals(MessagePriority.LOW.getX_priority(), mess.getHeader("X-Priority", null));
        Assert.assertEquals(MessagePriority.LOW.getImportance(), mess.getHeader("Importance", null));
        Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));

        MimeMultipart mixed = (MimeMultipart) mess.getContent();
        MimeMultipart related = (MimeMultipart) mixed.getBodyPart(0).getContent();
        MimeMultipart alternative = (MimeMultipart) related.getBodyPart(0).getContent();
        BodyPart attachment = mixed.getBodyPart(1);
        BodyPart inlineAttachment = related.getBodyPart(1);

        BodyPart textAlt = alternative.getBodyPart(0);
        BodyPart html = alternative.getBodyPart(1);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(2, mixed.getCount());

        Assert.assertTrue(related.getContentType().startsWith("multipart/related"));
        Assert.assertEquals(2, related.getCount());

        Assert.assertTrue(html.getContentType().startsWith("text/html"));
        Assert.assertEquals(htmlBody, MailTestUtil.getStringContent(html));

        Assert.assertTrue(textAlt.getContentType().startsWith("text/plain"));
        Assert.assertEquals(textBody, MailTestUtil.getStringContent(textAlt));

        Assert.assertTrue(attachment.getContentType().startsWith("text/plain"));
        Assert.assertEquals("template.text.velocity", attachment.getFileName());

        Assert.assertTrue(inlineAttachment.getContentType().startsWith("image/png;"));
        Assert.assertEquals("seamLogo.png", inlineAttachment.getFileName());
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testTextMailMessageLongFields() throws MessagingException, IOException {
        String subject = "Sometimes it is important to have a really long subject even if nobody is going to read it - "
                + java.util.UUID.randomUUID().toString();

        String longFromName = "FromSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow?";
        String longFromAddress = "sometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo@jboss.org";
        String longToName = "ToSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow?";
        String longToAddress = "toSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo.seamerson@seam-mail.test";
        String longCcName = "CCSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow? Hatty";
        String longCcAddress = "cCSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo.hatty@jboss.org";

        EmailMessage e;

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(longToName);
            person.setEmail(longToAddress);

            e = mailMessage.get().from(MailTestUtil.getAddressHeader(longFromName, longFromAddress))
                    .to(MailTestUtil.getAddressHeader(longToName, longToAddress))
                    .cc(MailTestUtil.getAddressHeader(longCcName, longCcAddress)).subject(subject).bodyText(textBody)
                    .importance(MessagePriority.HIGH).send(session.get());
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 2 got " + wiser.getMessages().size(), wiser
                .getMessages().size() == 2);

        MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals(MailTestUtil.getAddressHeader(longFromName, longFromAddress), mess.getHeader("From", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(longToName, longToAddress), mess.getHeader("To", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(longCcName, longCcAddress), mess.getHeader("CC", null));
        Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
        Assert.assertEquals(MessagePriority.HIGH.getPriority(), mess.getHeader("Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getX_priority(), mess.getHeader("X-Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getImportance(), mess.getHeader("Importance", null));
        Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));

        MimeMultipart mixed = (MimeMultipart) mess.getContent();
        BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(textBody, MailTestUtil.getStringContent(text));
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test(expected = SendFailedException.class)
    public void testTextMailMessageSendFailed() {
        String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();
        String messageId = "1234@seam.test.com";

        // Port is one off so this should fail
        Wiser wiser = new Wiser(mailConfig.getServerPort() + 1);
        wiser.setHostname(mailConfig.getServerHost());

        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            mailMessage.get().from(MailTestUtil.getAddressHeader(fromName, fromAddress)).replyTo(replyToAddress).to(toAddress)
                    .subject(subject).bodyText(textBody).importance(MessagePriority.HIGH).messageId(messageId)
                    .send(session.get());
        } finally {
            stop(wiser);
        }
    }

    @Test(expected = InvalidAddressException.class)
    public void testTextMailMessageInvalidAddress() throws SendFailedException {
        String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

        String messageId = "1234@seam.test.com";

        // Port is one off so this should fail
        Wiser wiser = new Wiser(mailConfig.getServerPort() + 1);
        wiser.setHostname(mailConfig.getServerHost());

        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            mailMessage.get().from("seam seamerson@test.com", fromName).replyTo(replyToAddress).to(toAddress, toName)
                    .subject(subject).bodyText(textBody).importance(MessagePriority.HIGH).messageId(messageId)
                    .send(session.get());
        } finally {
            stop(wiser);
        }
    }

    @Test
    public void testTextMailMessageUsingPerson() throws MessagingException, IOException {
        String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();
        String messageId = "1234@seam.test.com";

        EmailMessage e;

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            e = mailMessage.get().from(MailTestUtil.getAddressHeader(fromName, fromAddress)).replyTo(replyToAddress).to(person)
                    .subject(subject).bodyText(textBody).importance(MessagePriority.HIGH).messageId(messageId)
                    .send(session.get());
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser
                .getMessages().size() == 1);

        MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals(MailTestUtil.getAddressHeader(fromName, fromAddress), mess.getHeader("From", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(replyToAddress), mess.getHeader("Reply-To", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(toName, toAddress), mess.getHeader("To", null));
        Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
        Assert.assertEquals(MessagePriority.HIGH.getPriority(), mess.getHeader("Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getX_priority(), mess.getHeader("X-Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getImportance(), mess.getHeader("Importance", null));
        Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));
        Assert.assertEquals(messageId, MailUtility.headerStripper(mess.getHeader("Message-ID", null)));

        MimeMultipart mixed = (MimeMultipart) mess.getContent();
        BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(textBody, MailTestUtil.getStringContent(text));
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testTextMailMessageUsingDefaultSession() throws MessagingException, IOException {

        String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();
        String messageId = "1234@seam.test.com";

        EmailMessage e;

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            e = mailMessage.get().from(MailTestUtil.getAddressHeader(fromName, fromAddress)).replyTo(replyToAddress).to(person)
                    .subject(subject).bodyText(textBody).importance(MessagePriority.HIGH).messageId(messageId).send();
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser
                .getMessages().size() == 1);

        MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals(MailTestUtil.getAddressHeader(fromName, fromAddress), mess.getHeader("From", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(replyToAddress), mess.getHeader("Reply-To", null));
        Assert.assertEquals(MailTestUtil.getAddressHeader(toName, toAddress), mess.getHeader("To", null));
        Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
        Assert.assertEquals(MessagePriority.HIGH.getPriority(), mess.getHeader("Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getX_priority(), mess.getHeader("X-Priority", null));
        Assert.assertEquals(MessagePriority.HIGH.getImportance(), mess.getHeader("Importance", null));
        Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));
        Assert.assertEquals(messageId, MailUtility.headerStripper(mess.getHeader("Message-ID", null)));

        MimeMultipart mixed = (MimeMultipart) mess.getContent();
        BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(textBody, MailTestUtil.getStringContent(text));
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    /**
     * Wiser takes a fraction of a second to shutdown, so let it finish.
     */
    protected void stop(Wiser wiser) {
        wiser.stop();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
