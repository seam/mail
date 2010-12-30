package org.jboss.seam.mail.core;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import javax.inject.Inject;
import javax.mail.Session;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.solder.resourceLoader.ResourceProvider;

public class MailMessageImpl implements MailMessage
{
   private EmailMessage emailMessage;

   @Inject
   private ResourceProvider resourceProvider;

   public MailMessageImpl()
   {
      emailMessage = new EmailMessage();
   }
   
   public MailMessageImpl(String rootSubType)
   {
      emailMessage = new EmailMessage(rootSubType);
   }

   // Begin Addressing

   public MailMessage from(String address)
   {
      emailMessage.setFromAddress(new EmailContact(address));
      return this;
   }

   public MailMessage from(String name, String address)
   {
      emailMessage.setFromAddress(new EmailContact(name, address));
      return this;
   }

   public MailMessage from(EmailContact emailContact)
   {
      emailMessage.setFromAddress(emailContact);
      return this;
   }

   public MailMessage replyTo(String address)
   {
      emailMessage.addReplyToAddress(new EmailContact(address));
      return this;
   }

   public MailMessage replyTo(String name, String address)
   {
      emailMessage.addReplyToAddress(new EmailContact(name, address));
      return this;
   }

   public MailMessage replyTo(EmailContact emailContact)
   {
      emailMessage.addReplyToAddress(emailContact);
      return this;
   }

   public MailMessage replyTo(Collection<EmailContact> emailContacts)
   {
      emailMessage.addReplyToAddresses(emailContacts);
      return this;
   }

   public MailMessage to(String address)
   {
      emailMessage.addToAddress(new EmailContact(address));
      return this;
   }

   public MailMessage to(String name, String address)
   {
      emailMessage.addToAddress(new EmailContact(name, address));
      return this;
   }

   public MailMessage to(EmailContact emailContact)
   {
      emailMessage.addToAddress(emailContact);
      return this;
   }

   public MailMessage to(Collection<EmailContact> emailContacts)
   {
      emailMessage.addToAddresses(emailContacts);
      return this;
   }

   public MailMessage cc(String address)
   {
      emailMessage.addCcAddress(new EmailContact(address));
      return this;
   }

   public MailMessage cc(String name, String address)
   {
      emailMessage.addCcAddress(new EmailContact(name, address));
      return this;
   }

   public MailMessage cc(EmailContact emailContact)
   {
      emailMessage.addCcAddress(emailContact);
      return this;
   }

   public MailMessage cc(Collection<EmailContact> emailContacts)
   {
      emailMessage.addCcAddresses(emailContacts);
      return this;
   }

   public MailMessage bcc(String address)
   {
      emailMessage.addBccAddress(new EmailContact(address));
      return this;
   }

   public MailMessage bcc(String name, String address)
   {
      emailMessage.addBccAddress(new EmailContact(name, address));
      return this;
   }

   public MailMessage bcc(EmailContact emailContact)
   {
      emailMessage.addBccAddress(emailContact);
      return this;
   }

   public MailMessage bcc(Collection<EmailContact> emailContacts)
   {
      emailMessage.addBccAddresses(emailContacts);
      return this;
   }

   // End Addressing

   public MailMessage subject(String value)
   {
      emailMessage.setSubject(value);
      return this;
   }

   public MailMessage deliveryReceipt(String address)
   {
      emailMessage.addDeliveryReceiptAddress(address);
      return this;
   }

   public MailMessage readReceipt(String address)
   {
      emailMessage.addReadReceiptAddress(address);
      return this;
   }

   public MailMessage importance(MessagePriority messagePriority)
   {
      emailMessage.setImportance(messagePriority);
      return this;
   }

   public MailMessage messageId(String messageId)
   {
      emailMessage.setMessageId(messageId);
      return this;
   }

   public MailMessage textBody(String text)
   {
      emailMessage.setTextBody(text);
      return this;
   }

   public MailMessage htmlBody(String html)
   {
      emailMessage.setHtmlBody(html);
      return this;
   }

   public MailMessage htmlBodyTextAlt(String html, String text)
   {
      emailMessage.setTextBody(text);
      emailMessage.setHtmlBody(html);
      return this;
   }

   // Begin Attachments

   public MailMessage addAttachment(File file, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(MailUtility.getEmailAttachment(file, contentDisposition));
      return this;
   }

   public MailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      InputStream inputStream = resourceProvider.loadResourceStream(fileName);
      emailMessage.addAttachment(MailUtility.getEmailAttachment(fileName, inputStream, mimeType, contentDisposition));
      return this;
   }

   public MailMessage addAttachment(URL url, String fileName, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(MailUtility.getEmailAttachment(url, fileName, contentDisposition));
      return this;
   }

   public MailMessage addAttachment(byte[] bytes, String fileName, String mimeType, String contentClass, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(MailUtility.getEmailAttachment(bytes, fileName, mimeType, contentClass, contentDisposition));
      return this;
   }

   public MailMessage addAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(MailUtility.getEmailAttachment(bytes, fileName, mimeType, contentDisposition));
      return this;
   }

   public MailMessage addAttachment(EmailAttachment attachment)
   {
      emailMessage.addAttachment(attachment);
      return this;
   }

   public MailMessage addAttachment(Collection<EmailAttachment> attachments)
   {
      emailMessage.addAttachments(attachments);
      return this;
   }

   // End Attachments

   // Begin Calendar

   public MailMessage iCal(String html, byte[] bytes)
   {
      emailMessage.setHtmlBody(html);
      emailMessage.addAttachment(MailUtility.getEmailAttachment(bytes, null, "text/calendar;method=CANCEL", "urn:content-classes:calendarmessage", ContentDisposition.INLINE));
      return this;
   }

   // End Calendar

   public EmailMessage send(Session session)
   {
      MailUtility.send(emailMessage, session);

      return emailMessage;
   }
}
