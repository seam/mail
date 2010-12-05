package org.jboss.seam.mail.core;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;
import javax.mail.Session;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;

public class MailMessageImpl extends BaseMailMessage implements MailMessage
{
   @Inject
   public MailMessageImpl(Session session)
   {
      super(session);
   }
   
   public MailMessage from(String address)
   {
      super.setFrom(address);
      return this;
   }
   
   public MailMessage from(String name, String address)
   {
      super.setFrom(name, address);
      return this;
   }
   
   public MailMessage replyTo(String address)
   {
      super.setReplyTo(address);
      return this;
   }
   
   public MailMessage replyTo(String name, String address)
   {
      super.setReplyTo(name, address);
      return this;
   }

   public MailMessage to(String name, String address)
   {
      super.addTo(name, address);
      return this;
   }
   
   public MailMessage cc(String name, String address)
   {
      super.addCc(name, address);
      return this;
   }
   
   public MailMessage bcc(String name, String address)
   {
      super.addBcc(name, address);
      return this;
   }
   
   public MailMessage subject(String value)
   {
      super.setSubject(value);
      return this;
   }  

   public MailMessage deliveryReciept(String address)
   {
      super.setDeliveryReciept(address);
      return this;
   }
   
   public MailMessage readReciept(String address)
   {
      super.setReadReciept(address);
      return this;
   }   

   public MailMessage importance(MessagePriority messagePriority)
   {
      super.setImportance(messagePriority);
      return this;
   }
   
   public MailMessage textBody(String text)
   {
      super.setText(text);
      return this;      
   }
   
   public MailMessage htmlBody(String html)
   {
      super.setHTML(html);
      return this;
   }
   
   public MailMessage htmlBodyTextAlt(String html, String text)
   {
      super.setHTMLTextAlt(html, text);
      return this;
   }

   public MailMessage addAttachment(File file, ContentDisposition contentDisposition)
   {
      super.addAttachmentImpl(file, contentDisposition);
      return this;
   }

   public MailMessage addAttachment(String fileName, ContentDisposition contentDisposition)
   {
      super.addAttachmentImpl(fileName, contentDisposition);
      return this;
   }

   public MailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      super.addAttachmentImpl(fileName, mimeType, contentDisposition);
      return this;
   }

   public MailMessage addAttachment(URL url, String fileName, ContentDisposition contentDisposition)
   {
      super.addAttachmentImpl(url, fileName, contentDisposition);
      return this;
   }      
}
