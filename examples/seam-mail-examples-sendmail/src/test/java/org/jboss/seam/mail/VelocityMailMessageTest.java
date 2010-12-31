package org.jboss.seam.mail;

import java.io.IOException;
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
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.MailTestUtil;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.example.Gmail;
import org.jboss.seam.mail.example.Person;
import org.jboss.seam.mail.templating.VelocityMailMessage;
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

@RunWith(Arquillian.class)
public class VelocityMailMessageTest
{
   @Deployment
   public static Archive<?> createTestArchive()
   {
      Archive<?> ar = ShrinkWrap.create(WebArchive.class, "test.war")
      .addResource("template.text.vm", "WEB-INF/classes/template.text.vm")
      .addResource("template.html.vm", "WEB-INF/classes/template.text.vm")
      .addPackages(true, VelocityMailMessageTest.class.getPackage())
      .addLibraries(MavenArtifactResolver.resolve("org.jboss.seam.solder:seam-solder:3.0.0.Beta1"),
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
   private Session session;
   
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
   public void testVelocityTextMailMessage() throws IOException, MessagingException
   {
      String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(8977);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      try
      {
         wiser.start();
         
   
         person.setName(toName);
         person.setEmail(toAddress);
   
         velocityMailMessage.get()
            .from(fromName, fromAddress)
            .replyTo(replyToAddress)
            .to(toName, toAddress)
            .subject(subject)
            .templateTextFromClassPath("template.text.vm")
            .put("version", "Seam 3")
            .importance(MessagePriority.HIGH)
            .send(session);
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
      
      // TODO Verify MimeBodyPart hierarchy and $person resolution is happening.
   }

   @Test
   public void testVelocityHTMLMailMessage() throws IOException, MessagingException
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
            .from(fromName, fromAddress)
            .replyTo(replyToName, replyToAddress)
            .to(person.getName(), person.getEmail())
            .subject(subject)
            .templateHTMLFromClassPath("template.html.vm")
            .put("version", "Seam 3")
            .importance(MessagePriority.HIGH)
            .addAttachment(new URL("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png"), "seamLogo.png", ContentDisposition.INLINE)
            .send(session);
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
   public void testVelocityHTMLTextAltMailMessage() throws IOException, MessagingException
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
            .from(fromName, fromAddress)
            .to(person.getName(), person.getEmail())
            .subject(subject)
            .put("version", "Seam 3")
            .templateHTMLTextAltFromClassPath("template.html.vm", "template.text.vm")
            .importance(MessagePriority.LOW)
            .deliveryReceipt(fromAddress)
            .readReceipt("seam.test")
            .addAttachment("template.html.vm", "text/html", ContentDisposition.ATTACHMENT)
            .addAttachment(new URL("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png"), "seamLogo.png", ContentDisposition.INLINE)
            .send(session);
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
   public void testSMTPSessionAuthentication() throws IOException, MessagingException
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
            .from(fromName, fromAddress)
            .to(person.getName(), person.getEmail())
            .subject(subject)
            .put("version", "Seam 3")
            .templateHTMLTextAltFromClassPath("template.html.vm", "template.text.vm")
            .importance(MessagePriority.LOW)
            .deliveryReceipt(fromAddress)
            .readReceipt("seam.test")
            .addAttachment("template.html.vm", "text/html", ContentDisposition.ATTACHMENT)
            .addAttachment(new URL("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png"), "seamLogo.png", ContentDisposition.INLINE)
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
