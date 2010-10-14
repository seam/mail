package org.jboss.seam.mail;

import java.io.IOException;
import java.net.URL;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.MailTestUtil;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.example.Person;
import org.jboss.seam.mail.templating.VelocityMailMessage;
import org.jboss.seam.mail.util.MavenArtifactResolver;
import org.jboss.seam.mail.util.SMTPAuthenticator;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
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
      Archive<?> ar = ShrinkWrap.create(WebArchive.class, "test.war").addResource("template.text.vm", "WEB-INF/classes/template.text.vm").addResource("template.html.vm", "WEB-INF/classes/template.text.vm").addPackages(true, VelocityMailMessageTest.class.getPackage()).addLibrary(MavenArtifactResolver.resolve("org.jboss.weld:weld-extensions:1.0.0.Alpha2")).addWebResource(new ByteArrayAsset(new byte[0]), "beans.xml");
      System.out.println(ar.toString(true));
      return ar;
   }

   @Inject
   private Instance<VelocityMailMessage> velocityMailMessage;

   @Inject
   MailConfig mailConfig;

   @Inject
   Person person;
   
   String fromName = "Seam Framework";
   String fromAddress = "seam@jboss.org";
   String toName = "Seamy Seamerson";
   String toAddress = "seamy.seamerson@seam-mail.test";

   @Test
   public void testVelocityTextMailMessage() throws IOException, MessagingException
   {

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(2525);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      wiser.start();
      
      String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      person.setName(toName);
      person.setEmail(toAddress);

      velocityMailMessage.get()
      .from(fromName, fromAddress)
      .to(toName, toAddress)
      .subject(subject)
      .setTemplateText("template.text.vm")
      .put("version", "Seam 3")
      .importance(MessagePriority.HIGH)
      .send();

      wiser.stop();

      Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser.getMessages().size() == 1);

      MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

      Assert.assertEquals(MailTestUtil.getAddressHeader(fromName, fromAddress), mess.getHeader("From", null));
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
      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(2525);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      wiser.start();
      
      String subject = "HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      person.setName(toName);
      person.setEmail(toAddress);

      velocityMailMessage.get()
      .from(fromName, fromAddress)
      .to(person.getName(), person.getEmail())
      .subject(subject)
      .setTemplateHTML("template.html.vm")
      .put("version", "Seam 3")
      .importance(MessagePriority.HIGH)
      .addAttachment(new URL("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png"), "seamLogo.png", ContentDisposition.INLINE)
      .send();

      wiser.stop();

      Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser.getMessages().size() == 1);

      MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

      Assert.assertEquals(MailTestUtil.getAddressHeader(fromName, fromAddress), mess.getHeader("From", null));
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
      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(2525);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      wiser.start();

      String subject = "HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      person.setName(toName);
      person.setEmail(toAddress);

      velocityMailMessage.get()
      .from(fromName, fromAddress)
      .to(person.getName(), person.getEmail())
      .subject(subject)
      .put("version", "Seam 3")
      .setTemplateHTMLTextAlt("template.html.vm", "template.text.vm")
      .importance(MessagePriority.LOW)
      .deliveryReciept(fromAddress)
      .readReciept("seam.test")
      .addAttachment("template.html.vm", ContentDisposition.ATTACHMENT)
      .addAttachment(new URL("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png"), "seamLogo.png", ContentDisposition.INLINE)
      .send();

      wiser.stop();

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
   
   //TODO Enable this test when we have support for specialized MailConfig via XML
   @Ignore
   @Test
   public void testSMTPSessionAuthentication() throws IOException, MessagingException
   {
      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(3535);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      wiser.getServer().setAuthenticationHandlerFactory(new EasyAuthenticationHandlerFactory(new SMTPAuthenticator("test","test12!")));
      wiser.start();

      String subject = "HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      person.setName(toName);
      person.setEmail(toAddress);

      velocityMailMessage.get()
      .from(fromName, fromAddress)
      .to(person.getName(), person.getEmail())
      .subject(subject)
      .put("version", "Seam 3")
      .setTemplateHTMLTextAlt("template.html.vm", "template.text.vm")
      .importance(MessagePriority.LOW)
      .deliveryReciept(fromAddress)
      .readReciept("seam.test")
      .addAttachment("template.html.vm", ContentDisposition.ATTACHMENT)
      .addAttachment(new URL("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png"), "seamLogo.png", ContentDisposition.INLINE)
      .send();

      wiser.stop();

      Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + wiser.getMessages().size(), wiser.getMessages().size() == 1);

      MimeMessage mess = wiser.getMessages().get(0).getMimeMessage();

      Assert.assertEquals("Subject has been modified", subject, MimeUtility.unfold(mess.getHeader("Subject", null)));
   }
   
}
