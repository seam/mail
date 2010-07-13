package org.jboss.seam.mail;

import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.exception.SeamMailException;
import java.io.IOException;
import javax.inject.Inject;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.mail.core.Mail;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class VelocityMailMessageTest
{
   @Deployment
   public static Archive<?> createTestArchive() {
      Archive<?> ar = ShrinkWrap.create(WebArchive.class, "test.war")
         .addResource("template.text.vm", "WEB-INF/classes/template.text.vm")
         .addManifestResource("META-INF/seam-beans.xml", "seam-beans.xml")
         .addPackages(true, VelocityMailMessageTest.class.getPackage())
         .addLibrary(MavenArtifactResolver.resolve("org.jboss.weld:weld-extensions:1.0.0.Alpha2"))
         .addWebResource(new ByteArrayAsset(new byte[0]), "beans.xml");
      System.out.println(ar.toString(true));
      return ar;
   }

   @Inject Mail mail;

   @Test
   public void testGetVelocityMailMessage() throws SeamMailException, IOException
   {
      mail.velocity()
         .from("Seam Framework", "dallen@localhost")
         .to("Dan", "dallen@localhost")
         .subject("Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
         .setTemplateText("template.text.vm")
         .put("version", "Seam 3")
         .importance(MessagePriority.HIGH)
         .send();
   }
}
