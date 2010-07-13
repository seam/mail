package org.jboss.seam.mail;

import java.io.IOException;
import java.util.Iterator;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mail.core.Mail;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.MailTestUtil;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.exception.SeamMailException;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

@RunWith(Arquillian.class)
public class VelocityMailMessageTest
{
   @Deployment
   public static Archive<?> createTestArchive() {
      Archive<?> ar = ShrinkWrap.create(WebArchive.class, "test.war")
         .addResource("template.text.vm", "WEB-INF/classes/template.text.vm")
         .addPackages(true, VelocityMailMessageTest.class.getPackage())
         .addLibrary(MavenArtifactResolver.resolve("org.jboss.weld:weld-extensions:1.0.0.Alpha2"))
         .addWebResource(new ByteArrayAsset(new byte[0]), "beans.xml");
      System.out.println(ar.toString(true));
      return ar;
   }

   @Inject
   Mail mail;
   
   @Inject
   MailConfig mailConfig;
   
   @Inject 
   Person person;
   
   @SuppressWarnings("unchecked")
   @Test
   public void testGetVelocityTextMailMessage() throws SeamMailException, IOException
   { 
      
      mailConfig.setServerHost("localHost");
      mailConfig.setServerPort(2525);
      
      SimpleSmtpServer server = SimpleSmtpServer.start(mailConfig.getServerPort());

         
      String fromName = "Seam Framework";
      String fromAddress = "seam@jboss.org";
      String toName = "Seamy Seamerson";
      String toAddress = "cody.lerum@gmail.com";
      String subject = "Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString();
      
      person.setName(toName);
      person.setEmail(toAddress);

      mail.velocity()
         .from(fromName, fromAddress)
         .to(toName, toAddress)
         .subject(subject).setTemplateText("template.text.vm")
         .put("version", "Seam 3").importance(MessagePriority.HIGH)
         .send();

      server.stop();
      
      Assert.assertTrue("Didn't receive the expected amount of messages. Expected 1 got " + server.getReceivedEmailSize(), server.getReceivedEmailSize() == 1);
      
      Iterator emailIter = server.getReceivedEmail();
      SmtpMessage email = (SmtpMessage) emailIter.next();      

      Assert.assertTrue(email.getHeaderValue("From").equals(MailTestUtil.getAddressHeader(fromName, fromAddress)));
      Assert.assertTrue(email.getHeaderValue("To").equals(MailTestUtil.getAddressHeader(toName, toAddress)));
      Assert.assertTrue("Subject has been modified", email.getHeaderValue("Subject").equals(subject));
      Assert.assertTrue(email.getHeaderValue("Priority").equals(MessagePriority.HIGH.getPriority()));
      Assert.assertTrue(email.getHeaderValue("X-Priority").equals(MessagePriority.HIGH.getX_priority()));
      Assert.assertTrue(email.getHeaderValue("Importance").equals(MessagePriority.HIGH.getImportance()));
      Assert.assertTrue(email.getHeaderValue("Content-Type").startsWith("multipart/mixed"));
   }
}
