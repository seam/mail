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

import java.net.MalformedURLException;
import java.net.URL;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.InvalidAddressException;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.MailTestUtil;
import org.jboss.seam.mail.core.MailUtility;
import org.jboss.seam.mail.core.SendFailedException;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.example.Person;
import org.jboss.seam.mail.util.MavenArtifactResolver;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.subethamail.wiser.Wiser;
/**
 * 
 * @author Cody Lerum
 *
 */
@RunWith(Arquillian.class)
public class MailMessageTest
{
   @Deployment
   public static Archive<?> createTestArchive()
   {
      Archive<?> ar = ShrinkWrap.create(WebArchive.class, "test.war")
      .addResource("template.text.vm", "WEB-INF/classes/template.text.vm")
      .addPackages(true, MailMessageTest.class.getPackage())
      .addLibraries(MavenArtifactResolver.resolve("org.jboss.seam.solder:seam-solder:3.0.0.Beta2"),
            MavenArtifactResolver.resolve("org.subethamail:subethasmtp:3.1.4"),
            MavenArtifactResolver.resolve("org.apache.velocity:velocity:1.6.4"))
      .addWebResource(EmptyAsset.INSTANCE, "beans.xml");
      return ar;
   }

   @Inject
   private Instance<MailMessage> mailMessage;
   
   @Inject 
   private Instance<Session> session;

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

   String html = "<html><body><b>Hello</b> World!</body></html>";
   String text = "This is a Text Alternative";

   @Test
   public void testTextMailMessage() throws MessagingException
   {
      String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);
      
      String messageId = "1234@seam.test.com";

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      try
      {
         wiser.start();
   
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         mailMessage.get()
            .from(fromAddress, fromName)
            .replyTo(replyToAddress)
            .to(toAddress, toName)
            .subject(subject)
            .textBody(text)
            .importance(MessagePriority.HIGH)
            .messageId(messageId)
            .send(session.get());
      }
      finally
      {
         stop(wiser);
      }

      Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser.getMessages().size() == 1);

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


      // TODO Verify MimeBodyPart hierarchy and $person resolution is happening.
   }

   @Test
   public void testHTMLMailMessage() throws MalformedURLException, MessagingException
   {
      String subject = "HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);
      
      EmailMessage emailMessage;

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      try
      {
         wiser.start();
   
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         emailMessage = mailMessage.get()
            .from(fromAddress, fromName)
            .replyTo(replyToAddress, replyToName)
            .to(person.getEmail(), person.getName())
            .subject(subject)
            .htmlBody("<html><body>Hello World!</body></html>")
            .importance(MessagePriority.HIGH)
            .addAttachment(new URL("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png"), "seamLogo.png", ContentDisposition.INLINE)
            .send(session.get());
      }
      finally
      {
         stop(wiser);
      }

      Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser.getMessages().size() == 1);

      MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

      Assert.assertEquals(MailTestUtil.getAddressHeader(fromName, fromAddress), mess.getHeader("From", null));
      Assert.assertEquals(MailTestUtil.getAddressHeader(replyToName, replyToAddress), mess.getHeader("Reply-To", null));
      Assert.assertEquals(MailTestUtil.getAddressHeader(toName, toAddress), mess.getHeader("To", null));
      Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
      Assert.assertEquals(MessagePriority.HIGH.getPriority(), mess.getHeader("Priority", null));
      Assert.assertEquals(MessagePriority.HIGH.getX_priority(), mess.getHeader("X-Priority", null));
      Assert.assertEquals(MessagePriority.HIGH.getImportance(), mess.getHeader("Importance", null));
      Assert.assertEquals(emailMessage.getLastMessageId(), MailUtility.headerStripper(mess.getHeader("Message-ID", null)));
      Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));

      // TODO Verify MimeBodyPart hierarchy and $person resolution is happening.
   }

   @Test
   public void testHTMLTextAltMailMessage() throws MalformedURLException, MessagingException
   {
      String subject = "HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      try
      {
         wiser.start();
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         mailMessage.get()
            .from(fromAddress, fromName)
            .to(person.getEmail(), person.getName())
            .subject(subject)
            .htmlBodyTextAlt(html, text)
            .importance(MessagePriority.LOW)
            .deliveryReceipt(fromAddress)
            .readReceipt("seam.test")
            .addAttachment("template.text.vm", "text/plain", ContentDisposition.ATTACHMENT)
            .addAttachment(new URL("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png"), "seamLogo.png", ContentDisposition.INLINE)
            .send(session.get());
      }
      finally
      {
         stop(wiser);
      }

      Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser.getMessages().size() == 1);

      MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

      Assert.assertEquals(MailTestUtil.getAddressHeader(fromName, fromAddress), mess.getHeader("From", null));
      Assert.assertEquals(MailTestUtil.getAddressHeader(toName, toAddress), mess.getHeader("To", null));
      Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
      Assert.assertEquals(MessagePriority.LOW.getPriority(), mess.getHeader("Priority", null));
      Assert.assertEquals(MessagePriority.LOW.getX_priority(), mess.getHeader("X-Priority", null));
      Assert.assertEquals(MessagePriority.LOW.getImportance(), mess.getHeader("Importance", null));
      Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));

      // TODO Verify MimeBodyPart hierarchy and $person resolution is happening.
   }
   
   @Test
   public void testTextMailMessageLongFields() throws MessagingException
   {
      String subject = "Sometimes it is important to have a really long subject even if nobody is going to read it - " + java.util.UUID.randomUUID().toString();
      
      String longFromName = "FromSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow?";
      String longFromAddress = "sometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo@jboss.org";
      String longToName = "ToSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow?";
      String longToAddress = "toSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo.seamerson@seam-mail.test";
      String longCcName = "CCSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow? Hatty";
      String longCcAddress = "cCSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo.hatty@jboss.org";

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      try
      {
         wiser.start();
   
   
         person.setName(longToName);
         person.setEmail(longToAddress);
   
         mailMessage.get()
            .from(longFromAddress, longFromName)
            .to(longToAddress, longToName)
            .cc(longCcAddress, longCcName)
            .subject(subject)
            .textBody(text)
            .importance(MessagePriority.HIGH)
            .send(session.get());
      }
      finally
      {
         stop(wiser);
      }

      Assert.assertTrue("Didn't receive the expected amount of messages. Expected 2 got " + wiser.getMessages().size(), wiser.getMessages().size() == 2);

      MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

      Assert.assertEquals(MailTestUtil.getAddressHeader(longFromName, longFromAddress), mess.getHeader("From", null));
      Assert.assertEquals(MailTestUtil.getAddressHeader(longToName, longToAddress), mess.getHeader("To", null));
      Assert.assertEquals(MailTestUtil.getAddressHeader(longCcName, longCcAddress), mess.getHeader("CC", null));
      Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
      Assert.assertEquals(MessagePriority.HIGH.getPriority(), mess.getHeader("Priority", null));
      Assert.assertEquals(MessagePriority.HIGH.getX_priority(), mess.getHeader("X-Priority", null));
      Assert.assertEquals(MessagePriority.HIGH.getImportance(), mess.getHeader("Importance", null));
      Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));

      // TODO Verify MimeBodyPart hierarchy and $person resolution is happening.
   }
   
   @Test(expected=SendFailedException.class)
   public void testTextMailMessageSendFailed()
   {
      String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);
      
      String messageId = "1234@seam.test.com";

      //Port is one off so this should fail
      Wiser wiser = new Wiser(mailConfig.getServerPort()+1);
      
      try
      {
         wiser.start();
   
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         mailMessage.get()
            .from(fromAddress, fromName)
            .replyTo(replyToAddress)
            .to(toAddress, toName)
            .subject(subject)
            .textBody(text)
            .importance(MessagePriority.HIGH)
            .messageId(messageId)
            .send(session.get());
      }
      finally
      {
         stop(wiser);
      }      
   }
   
   @Test(expected=InvalidAddressException.class)
   public void testTextMailMessageInvalidAddress() throws SendFailedException
   {
      String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);
      
      String messageId = "1234@seam.test.com";

      //Port is one off so this should fail
      Wiser wiser = new Wiser(mailConfig.getServerPort()+1);
      
      try
      {
         wiser.start();
   
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         mailMessage.get()
            .from("seam seamerson@test.com", fromName)
            .replyTo(replyToAddress)
            .to(toAddress, toName)
            .subject(subject)
            .textBody(text)
            .importance(MessagePriority.HIGH)
            .messageId(messageId)
            .send(session.get());
      }
      finally
      {
         stop(wiser);
      }      
   }
   
   @Test
   public void testTextMailMessageUsingPerson() throws MessagingException
   {
      String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);
      
      String messageId = "1234@seam.test.com";

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      try
      {
         wiser.start();   
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         mailMessage.get()
            .from(fromAddress, fromName)
            .replyTo(replyToAddress)
            .to(person)
            .subject(subject)
            .textBody(text)
            .importance(MessagePriority.HIGH)
            .messageId(messageId)
            .send(session.get());
      }
      finally
      {
         stop(wiser);
      }

      Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser.getMessages().size() == 1);

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


      // TODO Verify MimeBodyPart hierarchy and $person resolution is happening. 
   }
   
   /**
    * Wiser takes a fraction of a second to shutdown, so let it finish.
    */
   protected void stop(Wiser wiser)
   {
      wiser.stop();
      try
      {
         Thread.sleep(100);
      }
      catch (InterruptedException e)
      {
         throw new RuntimeException(e);
      }
   }
}
