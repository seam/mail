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
import java.io.UnsupportedEncodingException;

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
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.SendFailedException;
import org.jboss.seam.mail.core.enumerations.ContentDisposition;
import org.jboss.seam.mail.core.enumerations.MessagePriority;
import org.jboss.seam.mail.templating.velocity.CDIVelocityContext;
import org.jboss.seam.mail.templating.velocity.VelocityTemplate;
import org.jboss.seam.mail.util.Deployments;
import org.jboss.seam.mail.util.EmailAttachmentUtil;
import org.jboss.seam.mail.util.MailTestUtil;
import org.jboss.seam.mail.util.MessageConverter;
import org.jboss.seam.mail.util.SMTPAuthenticator;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.solder.resourceLoader.ResourceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.subethamail.smtp.auth.EasyAuthenticationHandlerFactory;
import org.subethamail.wiser.Wiser;

/**
 * @author Cody Lerum
 */
@RunWith(Arquillian.class)
public class VelocityMailMessageTest {
    @Deployment(name = "velocity")
    public static Archive<?> createTestArchive() {
        return Deployments.baseVelocityDeployment().addAsResource("template.text.velocity")
                .addAsResource("template.html.velocity").addAsWebResource("seam-mail-logo.png")
                .addPackages(true, VelocityMailMessageTest.class.getPackage());
    }

    @Inject
    private Instance<MailMessage> mailMessage;

    @Inject
    private ResourceProvider resourceProvider;

    @Inject
    private Instance<CDIVelocityContext> cDIVelocityContext;

    @Inject
    private MailConfig mailConfig;

    @Inject
    private Instance<Session> session;

    @Gmail
    @Inject
    private Session gmailSession;

    @Inject
    private Person person;

    String fromName = "Seam Framework";
    String fromAddress = "seam@jboss.org";
    String replyToName = "No Reply";
    String replyToAddress = "no-reply@seam-mal.test";
    String toName = "Seamy Seamerson";
    String toAddress = "seamy.seamerson@seam-mail.test";

    @Test
    public void testVelocityTextMailMessage() throws MessagingException, IOException {

        EmailMessage e;

        String uuid = java.util.UUID.randomUUID().toString();
        String subjectTemplate = "Text Message from $version Mail - " + uuid;
        String version = "Seam 3";
        String subject = "Text Message from " + version + " Mail - " + uuid;

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            e = mailMessage
                    .get()
                    .from(MailTestUtil.getAddressHeader(fromName, fromAddress))
                    .replyTo(replyToAddress)
                    .to(MailTestUtil.getAddressHeader(toName, toAddress))
                    .subject(new VelocityTemplate(subjectTemplate, cDIVelocityContext.get()))
                    .bodyText(
                            new VelocityTemplate(resourceProvider.loadResourceStream("template.text.velocity"),
                                    cDIVelocityContext.get())).put("version", version).importance(MessagePriority.HIGH)
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

        MimeMultipart mixed = (MimeMultipart) mess.getContent();
        BodyPart text = mixed.getBodyPart(0);

        Assert.assertTrue(mixed.getContentType().startsWith("multipart/mixed"));
        Assert.assertEquals(1, mixed.getCount());

        Assert.assertTrue("Incorrect Charset: " + e.getCharset(),
                text.getContentType().startsWith("text/plain; charset=" + e.getCharset()));
        Assert.assertEquals(expectedTextBody(person.getName(), version), MailTestUtil.getStringContent(text));
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testTextMailMessageSpecialCharacters() throws MessagingException, IOException {

        EmailMessage e;

        String uuid = java.util.UUID.randomUUID().toString();
        String subjectTemplate = "Special Char ü from $version Mail - " + uuid;
        String version = "Seam 3";
        String subject = "Special Char ü from " + version + " Mail - " + uuid;
        String specialTextBody = "This is a Text Body with a special character - ü - $version";
        String mergedSpecialTextBody = "This is a Text Body with a special character - ü - " + version;

        String messageId = "1234@seam.test.com";

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            e = mailMessage.get().from(MailTestUtil.getAddressHeader(fromName, fromAddress)).replyTo(replyToAddress)
                    .to(MailTestUtil.getAddressHeader(toName, toAddress)).subject(new VelocityTemplate(subjectTemplate))
                    .bodyText(new VelocityTemplate(specialTextBody)).importance(MessagePriority.HIGH).messageId(messageId)
                    .put("version", version).send(session.get());
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
        Assert.assertEquals(mergedSpecialTextBody, MimeUtility.decodeText(MailTestUtil.getStringContent(text)));
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testVelocityHTMLMailMessage() throws MessagingException, IOException {
        String subject = "HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString();
        String version = "Seam 3";
        EmailMessage emailMessage;

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            emailMessage = mailMessage
                    .get()
                    .from(MailTestUtil.getAddressHeader(fromName, fromAddress))
                    .replyTo(MailTestUtil.getAddressHeader(replyToName, replyToAddress))
                    .to(person)
                    .subject(subject)
                    .bodyHtml(
                            new VelocityTemplate(resourceProvider.loadResourceStream("template.html.velocity"),
                                    cDIVelocityContext.get()))
                    .put("version", version)
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
        Assert.assertEquals(expectedHtmlBody(emailMessage, person.getName(), person.getEmail(), version),
                MailTestUtil.getStringContent(html));

        Assert.assertTrue(attachment1.getContentType().startsWith("image/png;"));
        Assert.assertEquals("seamLogo.png", attachment1.getFileName());
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testVelocityHTMLTextAltMailMessage() throws MessagingException, IOException {
        String subject = "HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();
        String version = "Seam 3";
        EmailMessage emailMessage;

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            emailMessage = mailMessage
                    .get()
                    .from(MailTestUtil.getAddressHeader(fromName, fromAddress))
                    .to(MailTestUtil.getAddressHeader(person.getName(), person.getEmail()))
                    .subject(subject)
                    .put("version", version)
                    .bodyHtmlTextAlt(
                            new VelocityTemplate(resourceProvider.loadResourceStream("template.html.velocity"),
                                    cDIVelocityContext.get()),
                            new VelocityTemplate(resourceProvider.loadResourceStream("template.text.velocity"),
                                    cDIVelocityContext.get()))
                    .importance(MessagePriority.LOW)
                    .deliveryReceipt(fromAddress)
                    .readReceipt("seam.test")
                    .addAttachment("template.html.velocity", "text/html", ContentDisposition.ATTACHMENT,
                            resourceProvider.loadResourceStream("template.html.velocity"))
                    .addAttachment(
                            new URLAttachment("http://design.jboss.org/seam/logo/final/seam_mail_85px.png", "seamLogo.png",
                                    ContentDisposition.INLINE)).send();
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
        Assert.assertEquals(expectedHtmlBody(emailMessage, person.getName(), person.getEmail(), version),
                MailTestUtil.getStringContent(html));

        Assert.assertTrue(textAlt.getContentType().startsWith("text/plain"));
        Assert.assertEquals(expectedTextBody(person.getName(), version), MailTestUtil.getStringContent(textAlt));

        Assert.assertTrue(attachment.getContentType().startsWith("text/html"));
        Assert.assertEquals("template.html.velocity", attachment.getFileName());

        Assert.assertTrue(inlineAttachment.getContentType().startsWith("image/png;"));
        Assert.assertEquals("seamLogo.png", inlineAttachment.getFileName());
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test
    public void testSMTPSessionAuthentication() throws MessagingException, IOException {
        String subject = "HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

        mailConfig.setServerHost("localhost");
        mailConfig.setServerPort(8978);

        Wiser wiser = new Wiser(mailConfig.getServerPort());
        wiser.setHostname(mailConfig.getServerHost());
        wiser.getServer().setAuthenticationHandlerFactory(
                new EasyAuthenticationHandlerFactory(new SMTPAuthenticator("test", "test12!")));
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            mailMessage
                    .get()
                    .from(fromAddress)
                    .to(person.getEmail())
                    .subject(subject)
                    .put("version", "Seam 3")
                    .bodyHtmlTextAlt(
                            new VelocityTemplate(resourceProvider.loadResourceStream("template.html.velocity"),
                                    cDIVelocityContext.get()),
                            new VelocityTemplate(resourceProvider.loadResourceStream("template.text.velocity"),
                                    cDIVelocityContext.get()))
                    .importance(MessagePriority.LOW)
                    .deliveryReceipt(fromAddress)
                    .readReceipt("seam.test")
                    .addAttachment("template.html.velocity", "text/html", ContentDisposition.ATTACHMENT,
                            resourceProvider.loadResourceStream("template.html.velocity"))
                    .addAttachment(
                            new URLAttachment("http://design.jboss.org/seam/logo/final/seam_mail_85px.png", "seamLogo.png",
                                    ContentDisposition.INLINE)).send(gmailSession);
        } finally {
            stop(wiser);
        }

        Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser
                .getMessages().size() == 1);

        MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

        Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
        EmailMessage convertedMessage = MessageConverter.convert(mess);
        Assert.assertEquals(convertedMessage.getSubject(), subject);
    }

    @Test(expected = SendFailedException.class)
    public void testVelocityTextMailMessageSendFailed() throws UnsupportedEncodingException {
        String uuid = java.util.UUID.randomUUID().toString();
        String subject = "Text Message from $version Mail - " + uuid;
        String version = "Seam 3";

        // Port is two off so this should fail
        Wiser wiser = new Wiser(mailConfig.getServerPort() + 2);
        wiser.setHostname(mailConfig.getServerHost());
        try {
            wiser.start();

            person.setName(toName);
            person.setEmail(toAddress);

            mailMessage
                    .get()
                    .from(fromAddress)
                    .replyTo(replyToAddress)
                    .to(toAddress)
                    .subject(new VelocityTemplate(subject, cDIVelocityContext.get()))
                    .bodyText(
                            new VelocityTemplate(resourceProvider.loadResourceStream("template.text.velocity"),
                                    cDIVelocityContext.get())).put("version", version).importance(MessagePriority.HIGH)
                    .send(session.get());
        } finally {
            stop(wiser);
        }
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

    private static String expectedHtmlBody(EmailMessage emailMessage, String name, String email, String version) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">" + "\r\n");
        sb.append("<body>" + "\r\n");
        sb.append("<p><b>Dear <a href=\"mailto:" + email + "\">" + name + "</a>,</b></p>" + "\r\n");
        sb.append("<p>This is an example <i>HTML</i> email sent by " + version + " and Velocity.</p>" + "\r\n");
        sb.append("<p><img src=\"cid:"
                + EmailAttachmentUtil.getEmailAttachmentMap(emailMessage.getAttachments()).get("seamLogo.png").getContentId()
                + "\" /></p>" + "\r\n");
        sb.append("<p>It has an alternative text body for mail readers that don't support html.</p>" + "\r\n");
        sb.append("</body>" + "\r\n");
        sb.append("</html>");

        return sb.toString();
    }

    private static String expectedTextBody(String name, String version) {
        StringBuilder sb = new StringBuilder();

        sb.append("Hello " + name + ",\r\n");
        sb.append("\r\n");
        sb.append("This is the alternative text body for mail readers that don't support html. This was sent with " + version
                + " and Velocity.");

        return sb.toString();
    }
}
