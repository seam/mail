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

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mail.core.ClassPathEmailAttachment;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.MailTestUtil;
import org.jboss.seam.mail.core.SendFailedException;
import org.jboss.seam.mail.core.URLEmailAttachment;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.example.Gmail;
import org.jboss.seam.mail.example.Person;
import org.jboss.seam.mail.templating.VelocityMailMessage;
import org.jboss.seam.mail.templating.velocity.VelocityClassPathTemplate;
import org.jboss.seam.mail.templating.velocity.VelocityTextTemplate;
import org.jboss.seam.mail.util.MavenArtifactResolver;
import org.jboss.seam.mail.util.SMTPAuthenticator;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.subethamail.smtp.auth.EasyAuthenticationHandlerFactory;
import org.subethamail.wiser.Wiser;
/**
 * 
 * @author Cody Lerum
 *
 */
@RunWith(Arquillian.class)
public class VelocityMailMessageTest
{
   @Deployment
   public static Archive<?> createTestArchive()
   {
      Archive<?> ar = ShrinkWrap.create(WebArchive.class, "test.war")
      .addResource("template.text.vm", "WEB-INF/classes/template.text.vm")
      .addResource("template.html.vm", "WEB-INF/classes/template.html.vm")
      .addPackages(true, VelocityMailMessageTest.class.getPackage())
      .addLibraries(MavenArtifactResolver.resolve("org.jboss.seam.solder:seam-solder:3.0.0.Beta4"),
            MavenArtifactResolver.resolve("org.subethamail:subethasmtp:3.1.4"),
            MavenArtifactResolver.resolve("org.apache.velocity:velocity:1.6.4"),
            MavenArtifactResolver.resolve("commons-lang:commons-lang:2.4"))
      .addWebResource(EmptyAsset.INSTANCE, "beans.xml");
      return ar;
   }

   @Inject
   private Instance<VelocityMailMessage> velocityMailMessage;

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
   public void testVelocityTextMailMessage() throws MessagingException
   {
      String uuid = java.util.UUID.randomUUID().toString();
      String subject = "Text Message from $version Mail - " + uuid;
      String version = "Seam 3";
      String mergedSubject = "Text Message from " + version + " Mail - " + uuid;

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      try
      {
         wiser.start();
         
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         velocityMailMessage.get()
            .from(fromAddress, fromName)
            .replyTo(replyToAddress)
            .to(toAddress, toName)
            .subject(new VelocityTextTemplate(subject))
            .bodyText(new VelocityClassPathTemplate("template.text.vm"))
            .put("version", version)
            .importance(MessagePriority.HIGH)
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
      Assert.assertEquals("Subject has been modified", mergedSubject, MimeUtility.unfold(mess.getHeader("Subject", null)));
      Assert.assertEquals(MessagePriority.HIGH.getPriority(), mess.getHeader("Priority", null));
      Assert.assertEquals(MessagePriority.HIGH.getX_priority(), mess.getHeader("X-Priority", null));
      Assert.assertEquals(MessagePriority.HIGH.getImportance(), mess.getHeader("Importance", null));
      Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));
      
      // TODO Verify MimeBodyPart hierarchy and $person resolution is happening.
   }

   @Test
   public void testVelocityHTMLMailMessage() throws MalformedURLException, MessagingException
   {
      String subject = "HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString();
    
      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      try
      {
         wiser.start();
         
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         velocityMailMessage.get()
            .from(fromAddress, fromName)
            .replyTo(replyToAddress, replyToName)
            .to(person)
            .subject(subject)
            .bodyHtml(new VelocityClassPathTemplate("template.html.vm"))
            .put("version", "Seam 3")
            .importance(MessagePriority.HIGH)
            .addAttachment(new URLEmailAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
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
      Assert.assertTrue(mess.getHeader("Content-Type", null).startsWith("multipart/mixed"));

      // TODO Verify MimeBodyPart hierarchy and $person resolution is happening.
   }

   @Test
   public void testVelocityHTMLTextAltMailMessage() throws MessagingException, MalformedURLException
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
   
         velocityMailMessage.get()
            .from(fromAddress, fromName)
            .to(person.getEmail(), person.getName())
            .subject(subject)
            .put("version", "Seam 3")
            .bodyHtmlTextAlt(new VelocityClassPathTemplate("template.html.vm"), new VelocityClassPathTemplate("template.text.vm"))
            .importance(MessagePriority.LOW)
            .deliveryReceipt(fromAddress)
            .readReceipt("seam.test")
            .addAttachment(new ClassPathEmailAttachment("template.html.vm", "text/html", ContentDisposition.ATTACHMENT))
            .addAttachment(new URLEmailAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
            .send();
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
   public void testSMTPSessionAuthentication() throws MessagingException, MalformedURLException
   {
      String subject = "HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();
     
      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8978);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      wiser.getServer().setAuthenticationHandlerFactory(new EasyAuthenticationHandlerFactory(new SMTPAuthenticator("test","test12!")));
      try
      {
         wiser.start();
   
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         velocityMailMessage.get()
            .from(fromAddress, fromName)
            .to(person.getEmail(), person.getName())
            .subject(subject)
            .put("version", "Seam 3")
            .bodyHtmlTextAlt(new VelocityClassPathTemplate("template.html.vm"), new VelocityClassPathTemplate("template.text.vm"))
            .importance(MessagePriority.LOW)
            .deliveryReceipt(fromAddress)
            .readReceipt("seam.test")
            .addAttachment(new ClassPathEmailAttachment("template.html.vm", "text/html", ContentDisposition.ATTACHMENT))
            .addAttachment(new URLEmailAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
            .send(gmailSession);
      }
      finally
      {
         stop(wiser);
      }

      Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser.getMessages().size() == 1);

      MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

      Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
   }
   
   @Test(expected=SendFailedException.class)
   public void testVelocityTextMailMessageSendFailed()
   {
      String uuid = java.util.UUID.randomUUID().toString();
      String subject = "Text Message from $version Mail - " + uuid;
      String version = "Seam 3";
      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);

      //Port is two off so this should fail
      Wiser wiser = new Wiser(mailConfig.getServerPort()+2);
      try
      {
         wiser.start();
         
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         velocityMailMessage.get()
            .from(fromAddress, fromName)
            .replyTo(replyToAddress)
            .to(toAddress, toName)
            .subject(new VelocityTextTemplate(subject))
            .bodyText(new VelocityClassPathTemplate("template.text.vm"))
            .put("version", version)
            .importance(MessagePriority.HIGH)
            .send(session.get());
      }
      finally
      {
         stop(wiser);
      }
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
