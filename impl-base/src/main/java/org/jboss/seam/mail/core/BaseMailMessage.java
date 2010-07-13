package org.jboss.seam.mail.core;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.activation.URLDataSource;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jboss.seam.mail.annotations.Module;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MailHeader;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.core.enumurations.RecipientType;
import org.jboss.seam.mail.exception.SeamMailException;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;

public abstract class BaseMailMessage<T extends MailMessage<T>> implements MailMessage<T>
{
   private RootMimeMessage rootMimeMessage;
   private String charset;
   private Map<String, Attachment> attachments = new HashMap<String, Attachment>();
   private MimeMultipart rootMultipart = new MimeMultipart("mixed");
   private MimeMultipart relatedMultipart = new MimeMultipart("related");

   @Inject
   private ResourceProvider resourceProvider;

   @Inject
   public BaseMailMessage(@Module Session session) throws SeamMailException
   {
      rootMimeMessage = new RootMimeMessage(session);
      charset = "UTF-8";
      setSentDate(new Date());
      setMessageID("<" + UUID.randomUUID().toString() + "@" + UUID.randomUUID().toString() + ">");
      initialize();
   }

   /**
    * Obtains the true underlying class type
    * 
    * @return
    */
   protected abstract Class<T> getRealClass();

   /**
    * Provides typesafe casting to the true return type of this instance
    * 
    * @return
    */
   protected final T covariantReturn()
   {
      return this.getRealClass().cast(this);
   }

   private void initialize() throws SeamMailException
   {
      try
      {
         rootMimeMessage.setContent(rootMultipart);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to set RootMultiPart", e);
      }
   }

   public T addRecipient(RecipientType recipientType, EmailContact emailContact) throws SeamMailException
   {
      try
      {
         rootMimeMessage.addRecipient(recipientType.getRecipientType(), MailUtility.getInternetAddress(emailContact));
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add recipient " + recipientType + ": " + emailContact.toString() + " to MIME message", e);
      }
      return this.covariantReturn();
   }

   public void addRecipients(RecipientType recipientType, EmailContact[] emailContacts) throws SeamMailException
   {
      try
      {
         rootMimeMessage.addRecipients(recipientType.getRecipientType(), MailUtility.getInternetAddressses(emailContacts));
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add " + recipientType + ":  Collection<Recipients>to MIME message", e);
      }
   }

   public void addRecipients(RecipientType recipientType, Collection<EmailContact> emailContacts) throws SeamMailException
   {
      try
      {
         rootMimeMessage.addRecipients(recipientType.getRecipientType(), MailUtility.getInternetAddressses(emailContacts));
      }
      catch (MessagingException e)
      {
      }
   }

   public T from(String name, String address) throws SeamMailException
   {
      setFrom(new EmailContact(name, address));
      return this.covariantReturn();
   }

   public T to(String name, String address) throws SeamMailException
   {
      addRecipient(RecipientType.TO, new EmailContact(name, address));
      return this.covariantReturn();
   }

   public T cc(String name, String address) throws SeamMailException
   {
      addRecipient(RecipientType.CC, new EmailContact(name, address));
      return this.covariantReturn();
   }

   public T bcc(String name, String address) throws SeamMailException
   {
      addRecipient(RecipientType.BCC, new EmailContact(name, address));
      return this.covariantReturn();
   }

   public T subject(String subject) throws SeamMailException
   {
      subject(subject, "UTF-8");
      return this.covariantReturn();
   }

   public void subject(String subject, String charset) throws SeamMailException
   {
      try
      {
         rootMimeMessage.setSubject(subject, charset);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add subject:" + subject + " to MIME message with charset: " + charset, e);
      }
   }

   public void setFrom(String name, String address) throws SeamMailException
   {
      setFrom(new EmailContact(name, address));
   }

   public T setFrom(EmailContact emailContact) throws SeamMailException
   {
      try
      {
         rootMimeMessage.setFrom(MailUtility.getInternetAddress(emailContact));
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add From Address:" + emailContact.getEmailAddress() + " to MIME message with charset: " + emailContact.getCharset(), e);
      }
      return this.covariantReturn();
   }

   public void setSentDate(Date date) throws SeamMailException
   {
      try
      {
         rootMimeMessage.setSentDate(date);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to set Sent Date on MimeMessage", e);
      }
   }

   public void setMessageID(String messageId)
   {
      rootMimeMessage.setMessageId(messageId);
   }

   public T deliveryReciept(String email) throws SeamMailException
   {
      setHeader(MailHeader.DELIVERY_RECIEPT.headerValue(), "<" + email + ">");
      return this.covariantReturn();
   }

   public T readReciept(String email) throws SeamMailException
   {
      setHeader(MailHeader.READ_RECIEPT.headerValue(), "<" + email + ">");
      return this.covariantReturn();
   }

   public T importance(MessagePriority messagePriority) throws SeamMailException
   {
      setHeader("X-Priority", messagePriority.getX_priority());
      setHeader("Priority", messagePriority.getPriority());
      setHeader("Importance", messagePriority.getImportance());
      return this.covariantReturn();
   }

   public void setHeader(String name, String value) throws SeamMailException
   {
      try
      {
         rootMimeMessage.setHeader(name, value);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to SET Header: + " + name + " to Value: " + value, e);
      }
   }

   public void addHeader(String name, String value) throws SeamMailException
   {
      try
      {
         rootMimeMessage.addHeader(name, value);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to ADD Header: + " + name + " to Value: " + value, e);
      }
   }

   public T setText(String text) throws SeamMailException
   {
      try
      {
         rootMultipart.addBodyPart(buildTextBodyPart(text));
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add TextBody to MimeMessage", e);
      }
      return this.covariantReturn();
   }

   public T setHTML(String html) throws SeamMailException
   {
      MimeBodyPart relatedBodyPart = new MimeBodyPart();
      try
      {
         relatedMultipart.addBodyPart(buildHTMLBodyPart(html));
         relatedBodyPart.setContent(relatedMultipart);
         rootMultipart.addBodyPart(relatedBodyPart);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to add TextBody to MimeMessage", e);
      }
      return this.covariantReturn();
   }

   public T setHTMLTextAlt(String html, String text) throws SeamMailException
   {
      MimeBodyPart mixedBodyPart = new MimeBodyPart();

      MimeBodyPart relatedBodyPart = new MimeBodyPart();

      MimeMultipart alternativeMultiPart = new MimeMultipart("alternative");

      try
      {
         // Text must be the first or some HTML capable clients will fail to
         // render HTML bodyPart.
         alternativeMultiPart.addBodyPart(buildTextBodyPart(text));
         alternativeMultiPart.addBodyPart(buildHTMLBodyPart(html));

         relatedBodyPart.setContent(alternativeMultiPart);

         relatedMultipart.addBodyPart(relatedBodyPart);

         mixedBodyPart.setContent(relatedMultipart);

         rootMultipart.addBodyPart(mixedBodyPart);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to build HTML+Text Email", e);
      }
      return this.covariantReturn();
   }

   private MimeBodyPart buildTextBodyPart(String text) throws SeamMailException
   {
      MimeBodyPart textBodyPart = new MimeBodyPart();

      try
      {
         textBodyPart.setDisposition(ContentDisposition.INLINE.headerValue());
         textBodyPart.setText(text, charset);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to build TextBodyPart", e);
      }

      return textBodyPart;
   }

   private MimeBodyPart buildHTMLBodyPart(String html) throws SeamMailException
   {
      MimeBodyPart htmlBodyPart = new MimeBodyPart();

      try
      {
         htmlBodyPart.setDisposition(ContentDisposition.INLINE.headerValue());
         htmlBodyPart.setText(html, charset, "html");
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to build HTMLBodyPart", e);
      }

      return htmlBodyPart;
   }

   public T addAttachment(File file, ContentDisposition contentDisposition) throws SeamMailException
   {
      Attachment attachment = new Attachment(file, file.getName(), contentDisposition);
      addAttachment(attachment);
      return this.covariantReturn();
   }

   public void addAttachment(File file, String fileName, ContentDisposition contentDisposition) throws SeamMailException
   {
      Attachment attachment = new Attachment(file, fileName, contentDisposition);
      addAttachment(attachment);
   }

   public void addAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition) throws SeamMailException
   {
      Attachment attachment = new Attachment(bytes, fileName, mimeType, contentDisposition);
      addAttachment(attachment);
   }

   public void addAttachment(byte[] bytes, String fileName, ContentDisposition contentDisposition) throws SeamMailException
   {
      Attachment attachment = new Attachment(bytes, fileName, "application/octetStream", contentDisposition);
      addAttachment(attachment);
   }

   public T addAttachment(String fileName, String mimeType, ContentDisposition contentDisposition) throws SeamMailException
   {
      InputStream inputStream = resourceProvider.loadResourceStream(fileName);
      
      if(inputStream == null)
      {
         throw new SeamMailException("InputStream was NULL for fileName: " + fileName);
      }

      try
      {
         Attachment attachment = new Attachment(inputStream, fileName, mimeType, contentDisposition);
         addAttachment(attachment);
      }
      catch (SeamMailException e)
      {
         throw new SeamMailException("Unable to Add STANDARD Attachment: " + fileName, e);
      }

      return this.covariantReturn();
   }
   
   public T addAttachment(String fileName, ContentDisposition contentDisposition) throws SeamMailException
   {
      return addAttachment(fileName, "application/octetStream", contentDisposition);
   }

   public T addAttachment(URL url, String fileName, ContentDisposition contentDisposition) throws SeamMailException
   {
      Attachment attachment = new Attachment(new URLDataSource(url), fileName, contentDisposition);
      addAttachment(attachment);
      return this.covariantReturn();
   }

   public void addAttachment(Attachment attachment) throws SeamMailException
   {
      attachments.put(attachment.getAttachmentFileName(), attachment);
   }

   public Map<String, Attachment> getAttachments()
   {
      return attachments;
   }

   public MimeMessage getRootMimeMessage()
   {
      return rootMimeMessage;
   }

   public void finalizeMessage() throws SeamMailException
   {
      addAttachmentsToMessage();
   }

   public MimeMessage getFinalizedMessage() throws SeamMailException
   {
      finalizeMessage();
      return getRootMimeMessage();
   }

   public void send() throws SeamMailException
   {
      try
      {
         finalizeMessage();
         Transport.send(rootMimeMessage);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Message Send Failed!", e);
      }
   }

   private void addAttachmentsToMessage() throws SeamMailException
   {
      for (Attachment a : attachments.values())
      {
         if (a.getContentDisposition() == ContentDisposition.ATTACHMENT)
         {
            try
            {
               rootMultipart.addBodyPart(a);
            }
            catch (MessagingException e)
            {
               throw new SeamMailException("Unable to Add STANDARD Attachment: " + a.getAttachmentFileName(), e);
            }
         }
         else if (a.getContentDisposition() == ContentDisposition.INLINE)
         {
            try
            {
               relatedMultipart.addBodyPart(a);
            }
            catch (MessagingException e)
            {
               throw new SeamMailException("Unable to Add INLINE Attachment: " + a.getAttachmentFileName(), e);
            }
         }
         else
         {
            throw new SeamMailException("Unsupported Attachment Content Disposition");
         }
      }
   }
}
