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
import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.MailTestUtil;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.example.Person;
import org.jboss.seam.mail.util.MavenArtifactResolver;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.subethamail.wiser.Wiser;

@RunWith(Arquillian.class)
public class MailMessageTest
{
   @Deployment
   public static Archive<?> createTestArchive()
   {
      Archive<?> ar = ShrinkWrap.create(WebArchive.class, "test.war")
      .addResource("template.text.vm", "WEB-INF/classes/template.text.vm")
      .addPackages(true, MailMessageTest.class.getPackage())
      .addLibrary(MavenArtifactResolver.resolve("org.jboss.weld:weld-extensions:1.0.0.Beta1"))
      .addWebResource(new ByteArrayAsset(new byte[0]), "beans.xml");
      System.out.println(ar.toString(true));
      return ar;
   }

   @Inject
   private Instance<MailMessage> mailMessage;   

   @Inject
   MailConfig mailConfig;

   @Inject
   Person person;

   String fromName = "Seam Framework";
   String fromAddress = "seam@jboss.org";
   String toName = "Seamy Seamerson";
   String toAddress = "seamy.seamerson@seam-mail.test";
   String ccName = "Red Hatty";
   String ccAddress = "red.hatty@jboss.org";

   String html = "<html><body><b>Hello</b> World!</body></html>";
   String text = "This is a Text Alternative";

   @Test
   public void testTextMailMessage() throws IOException, MessagingException
   {

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(2525);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      wiser.start();

      String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      person.setName(toName);
      person.setEmail(toAddress);

      mailMessage.get()
      .from(fromName, fromAddress)
      .to(toName, toAddress)
      .subject(subject)
      .textBody(text)
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
   public void testHTMLMailMessage() throws IOException, MessagingException
   {
      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(2525);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      wiser.start();

      String subject = "HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      person.setName(toName);
      person.setEmail(toAddress);

      mailMessage.get()
      .from(fromName, fromAddress)
      .to(person.getName(), person.getEmail())
      .subject(subject)
      .htmlBody("<html><body>Hello World!</body></html>")
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
   public void testHTMLTextAltMailMessage() throws IOException, MessagingException
   {
      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(2525);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      wiser.start();

      String subject = "HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();

      person.setName(toName);
      person.setEmail(toAddress);

      mailMessage.get()
      .from(fromName, fromAddress)
      .to(person.getName(), person.getEmail())
      .subject(subject).htmlBodyTextAlt(html, text)
      .importance(MessagePriority.LOW)
      .deliveryReciept(fromAddress)
      .readReciept("seam.test")
      .addAttachment("template.text.vm", ContentDisposition.ATTACHMENT)
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
   
   @Test
   public void testTextMailMessageLongFields() throws IOException, MessagingException
   {

      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(2525);

      Wiser wiser = new Wiser(mailConfig.getServerPort());
      wiser.start();

      String subject = "Sometimes it is important to have a really long subject even if nobody is going to read it - " + java.util.UUID.randomUUID().toString();
      
      String longFromName = "FromSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow?";
      String longFromAddress = "sometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo@jboss.org";
      String longToName = "ToSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow?";
      String longToAddress = "toSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo.seamerson@seam-mail.test";
      String longCcName = "CCSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo YouKnow? Hatty";
      String longCcAddress = "cCSometimesPeopleHaveNamesWhichAreALotLongerThanYouEverExpectedSomeoneToHaveSoItisGoodToTestUpTo100CharactersOrSo.hatty@jboss.org";

      person.setName(longToName);
      person.setEmail(longToAddress);

      mailMessage.get()
      .from(longFromName, longFromAddress)
      .to(longToName, longToAddress)
      .cc(longCcName, longCcAddress)
      .subject(subject)
      .textBody(text)
      .importance(MessagePriority.HIGH)
      .send();

      wiser.stop();

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
}
