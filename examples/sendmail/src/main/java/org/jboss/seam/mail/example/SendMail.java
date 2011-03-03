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

package org.jboss.seam.mail.example;

import java.net.MalformedURLException;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.mail.Session;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.attachments.ClassPathEmailAttachment;
import org.jboss.seam.mail.attachments.URLEmailAttachment;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.templating.ClassPathTemplate;
import org.jboss.seam.mail.templating.FreeMarkerMailMessage;
import org.jboss.seam.mail.templating.VelocityMailMessage;
/**
 * 
 * @author Cody Lerum
 *
 */
@Model
public class SendMail
{
   private String text = "Simple Message with text body";

   @Inject
   private Instance<MailMessage> mailMessage;
   
   @Inject
   private Instance<VelocityMailMessage> velocityMailMessage;
   
   @Inject
   private Instance<FreeMarkerMailMessage> freeMarkerMailMessage;
   
   @Inject
   private Instance<Session> session;
   
   @Inject
   private Person person;   

   public void sendText()
   {
      mailMessage.get()
            .from("seam@test.test", "Seam Framework")
            .to( person.getEmail(), person.getName())
            .subject("Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .textBody(text)
            .send();
   }
   
   public void sendHTMLFreeMarker() throws MalformedURLException
   {      
      freeMarkerMailMessage.get()
            .from("seam@test.test", "Seam Framework")
            .to(person)
            .subject("HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .bodyHtml(new ClassPathTemplate("template.html.freemarker"))
            .put("person", person)
            .put("version", "Seam 3")
            .importance(MessagePriority.HIGH)
            .addAttachment(new URLEmailAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
            .send(session.get());
   }

   public void sendHTMLwithAlternativeFreeMarker() throws MalformedURLException
   {
      freeMarkerMailMessage.get()
            .from("seam@test.test", "Seam Framework")
            .to(person.getEmail(), person.getName())
            .subject("HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .put("person", person)
            .put("version", "Seam 3")
            .bodyHtmlTextAlt(new ClassPathTemplate("template.html.freemarker"), new ClassPathTemplate("template.text.freemarker"))
            .importance(MessagePriority.LOW)
            .deliveryReceipt("seam@jboss.org")
            .readReceipt("seam@jboss.org")
            .addAttachment(new ClassPathEmailAttachment("template.html.freemarker", "text/html", ContentDisposition.ATTACHMENT))
            .addAttachment(new URLEmailAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
            .send(session.get());
   }

   public void sendHTMLVelocity() throws MalformedURLException
   {      
      velocityMailMessage.get()
            .from("seam@test.test", "Seam Framework")
            .to(person)
            .subject("HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .bodyHtml(new ClassPathTemplate("template.html.velocity"))
            .put("version", "Seam 3")
            .importance(MessagePriority.HIGH)
            .addAttachment(new URLEmailAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
            .send(session.get());
   }

   public void sendHTMLwithAlternativeVelocity() throws MalformedURLException
   {
      velocityMailMessage.get()
            .from("seam@test.test", "Seam Framework")
            .to(person.getEmail(), person.getName())
            .subject("HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .put("version", "Seam 3")
            .bodyHtmlTextAlt(new ClassPathTemplate("template.html.velocity"), new ClassPathTemplate("template.text.velocity"))
            .importance(MessagePriority.LOW)
            .deliveryReceipt("seam@jboss.org")
            .readReceipt("seam@jboss.org")
            .addAttachment(new ClassPathEmailAttachment("template.html.velocity", "text/html", ContentDisposition.ATTACHMENT))
            .addAttachment(new URLEmailAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
            .send(session.get());
   }
}
