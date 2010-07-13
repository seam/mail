package org.jboss.seam.mail;

import java.io.File;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import org.jboss.seam.mail.core.Mail;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.exception.SeamMailException;
import org.jboss.seam.mail.templating.exception.SeamTemplatingException;

@Model
public class HelloWorld
{
   private String name;
   private String email;

   private String text = "This is the alternative text body for mail readers that don't support html";

   @Inject
   private Mail mail;

   @NotNull
   @NotEmpty
   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   @NotNull
   @NotEmpty
   @Email
   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public void sendText() throws SeamMailException
   {
      mail.standard()
            .from("Seam Framework", "dallen@localhost")
            .to(name, email)
            .subject("Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .setText(text)
            .send();
   }

   public void sendHTML() throws SeamMailException, SeamTemplatingException
   {
      mail.velocity()
            .from("Seam Framework", "dallen@localhost")
            .to(name, email)
            .subject("HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .setTemplateHTML("src/main/resources/template.html.vm")
            .put("version", "Seam 3")
            .importance(MessagePriority.HIGH)
            .addAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE)
            .send();
   }

   public void sendHTMLwithAlternative() throws SeamMailException, SeamTemplatingException
   {
      mail.velocity()
            .from("Seam Framework", "dallen@localhost")
            .to(name, email)
            .subject("HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .put("version", "Seam 3")
            .setTemplateHTMLTextAlt("src/main/resources/template.html.vm", "src/main/resources/template.text.vm")
            .importance(MessagePriority.LOW)
            .deliveryReciept("cody.lerum@clearfly.net")
            .readReciept("cody.lerum@clearfly.net")
            .addAttachment(new File("src/main/resources/template.html.vm"), ContentDisposition.ATTACHMENT)
            .addAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE)
            .send();
   }
}
