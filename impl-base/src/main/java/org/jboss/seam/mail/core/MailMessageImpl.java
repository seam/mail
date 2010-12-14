package org.jboss.seam.mail.core;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import javax.inject.Inject;
import javax.mail.Session;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.core.enumurations.RecipientType;

public class MailMessageImpl extends BaseMailMessage implements MailMessage
{
   @Inject
   public MailMessageImpl(Session session)
   {
      super(session);
   }

   // Begin Addressing

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

   public MailMessage from(EmailContact emailContact)
   {
      super.setFrom(emailContact);
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

   public MailMessage replyTo(EmailContact emailContact)
   {
      super.setReplyTo(emailContact);
      return this;
   }

   public MailMessage to(String address)
   {
      super.addRecipient(RecipientType.TO, address);
      return this;
   }

   public MailMessage to(String name, String address)
   {
      super.addRecipient(RecipientType.TO, name, address);
      return this;
   }

   public MailMessage to(EmailContact emailContact)
   {
      super.addRecipient(RecipientType.TO, emailContact);
      return this;
   }

   public MailMessage to(Collection<EmailContact> emailContacts)
   {
      super.addRecipients(RecipientType.TO, emailContacts);
      return this;
   }

   public MailMessage cc(String address)
   {
      super.addRecipient(RecipientType.CC, address);
      return this;
   }

   public MailMessage cc(String name, String address)
   {
      super.addRecipient(RecipientType.CC, name, address);
      return this;
   }

   public MailMessage cc(EmailContact emailContact)
   {
      super.addRecipient(RecipientType.CC, emailContact);
      return this;
   }

   public MailMessage cc(Collection<EmailContact> emailContacts)
   {
      super.addRecipients(RecipientType.CC, emailContacts);
      return this;
   }

   public MailMessage bcc(String address)
   {
      super.addRecipient(RecipientType.BCC, address);
      return this;
   }

   public MailMessage bcc(String name, String address)
   {
      super.addRecipient(RecipientType.BCC, name, address);
      return this;
   }

   public MailMessage bcc(EmailContact emailContact)
   {
      super.addRecipient(RecipientType.BCC, emailContact);
      return this;
   }

   public MailMessage bcc(Collection<EmailContact> emailContacts)
   {
      super.addRecipients(RecipientType.BCC, emailContacts);
      return this;
   }

   // End Addressing

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

   //Begin Attachments
   
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

   public MailMessage addAttachment(byte[] bytes, String fileName, String mimeType, String contentClass, ContentDisposition contentDisposition)
   {
      super.addAttachmentImpl(bytes, fileName, mimeType, contentClass, contentDisposition);
      return this;
   }

   public MailMessage addAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      super.addAttachmentImpl(bytes, fileName, mimeType, null, contentDisposition);
      return this;
   }
   
   public MailMessage addAttachment(Attachment attachment)
   {
      super.addAttachmentImpl(attachment);
      return this;
   }
   
   //End Attachments
   
   //Begin Calendar
   
   public MailMessage calendarBody(String html, byte[] bytes)
   {
      super.setCalendar(html, new Attachment(bytes,null, "text/calendar;method=CANCEL", "urn:content-classes:calendarmessage", ContentDisposition.INLINE));
      return this;
   }
   
   //End Calendar

}
