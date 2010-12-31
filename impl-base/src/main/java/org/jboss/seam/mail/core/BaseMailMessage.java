package org.jboss.seam.mail.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MailHeader;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.core.enumurations.RecipientType;

public class BaseMailMessage
{
   private RootMimeMessage rootMimeMessage;
   private String charset;
   private String rootSubType;
   private Map<String, AttachmentPart> attachments = new HashMap<String, AttachmentPart>();
   private MimeMultipart rootMultipart;
   private MimeMultipart relatedMultipart = new MimeMultipart("related");
   private Session session;  
   
   public BaseMailMessage(Session session, String rootSubType)
   {
      this.session = session;
      this.rootSubType = rootSubType;      
      initialize();
   }
   
   public BaseMailMessage(Session session)
   {
      this(session, "mixed");
   }

   private void initialize()
   {
      rootMimeMessage = new RootMimeMessage(session);
      rootMultipart = new MimeMultipart(rootSubType);
      charset = "UTF-8";
      setSentDate(new Date());

      try
      {
         rootMimeMessage.setContent(rootMultipart);
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to set RootMultiPart", e);
      }

      initializeMessageId();
   }

   public void addRecipient(RecipientType recipientType, String address)
   {
      try
      {
         rootMimeMessage.addRecipient(recipientType.getRecipientType(), MailUtility.getInternetAddress(new EmailContact(address)));
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to add recipient " + recipientType + ": " + address + " to MIME message", e);
      }
   }

   public void addRecipient(RecipientType recipientType, String name, String address)
   {
      try
      {
         rootMimeMessage.addRecipient(recipientType.getRecipientType(), MailUtility.getInternetAddress(new EmailContact(name, address)));
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to add recipient " + recipientType + ": " + address + " to MIME message", e);
      }
   }

   public void addRecipient(RecipientType recipientType, EmailContact emailContact)
   {
      try
      {
         rootMimeMessage.addRecipient(recipientType.getRecipientType(), MailUtility.getInternetAddress(emailContact));
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to add recipient " + recipientType + ": " + emailContact.toString() + " to MIME message", e);
      }
   }

   public void addRecipients(RecipientType recipientType, EmailContact[] emailContacts)
   {
      try
      {
         rootMimeMessage.addRecipients(recipientType.getRecipientType(), MailUtility.getInternetAddressses(emailContacts));
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to add " + recipientType + ":  Collection<Recipients>to MIME message", e);
      }
   }

   public void addRecipients(RecipientType recipientType, Collection<EmailContact> emailContacts)
   {
      try
      {
         rootMimeMessage.addRecipients(recipientType.getRecipientType(), MailUtility.getInternetAddressses(emailContacts));
      }
      catch (MessagingException e)
      {
      }
   }

   public void setFrom(String address)
   {
      setFrom(new EmailContact(address));
   }

   public void setFrom(String name, String address)
   {
      setFrom(new EmailContact(name, address));
   }

   public BaseMailMessage setFrom(EmailContact emailContact)
   {
      try
      {
         rootMimeMessage.setFrom(MailUtility.getInternetAddress(emailContact));
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to add From Address:" + emailContact.getEmailAddress() + " to MIME message with charset: " + emailContact.getCharset(), e);
      }
      return this;
   }

   public void setReplyTo(String address)
   {
      setReplyTo(new EmailContact(address));
   }

   public void setReplyTo(String name, String address)
   {
      setReplyTo(new EmailContact(name, address));
   }

   public void setReplyTo(EmailContact emailContact)
   {
      List<EmailContact> emailContacts = new ArrayList<EmailContact>();
      emailContacts.add(emailContact);
      setReplyTo(emailContacts);
   }

   public void setReplyTo(Collection<EmailContact> emailContacts)
   {
      try
      {
         rootMimeMessage.setReplyTo(MailUtility.getInternetAddressses(emailContacts));
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to set Reply-To", e);
      }
   }

   public void setSubject(String value)
   {
      setSubject(value, "UTF-8");
   }

   public void setSubject(String value, String charset)
   {
      try
      {
         rootMimeMessage.setSubject(value, charset);
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to add subject:" + value + " to MIME message with charset: " + charset, e);
      }
   }

   public void setSentDate(Date date)
   {
      try
      {
         rootMimeMessage.setSentDate(date);
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to set Sent Date on MimeMessage", e);
      }
   }

   public void setMessageID(String messageId)
   {
      rootMimeMessage.setMessageId("<" + messageId + ">");
   }

   private void initializeMessageId()
   {
      String mailerDomainName = session.getProperty("mail.seam.domainName");

      if (mailerDomainName != null && mailerDomainName.length() > 0)
      {
         setMessageID(UUID.randomUUID().toString() + "@" + mailerDomainName);
      }
      else
      {
         setMessageID(UUID.randomUUID().toString() + "@" + MailUtility.getHostName());
      }
   }

   public void addDeliveryRecieptAddresses(Collection<String> addresses)
   {
      for (String address : addresses)
      {
         addDeliveryReciept(address);
      }
   }

   public void addReadRecieptAddresses(Collection<String> addresses)
   {
      for (String address : addresses)
      {
         addReadReciept(address);
      }
   }

   public void addDeliveryReciept(String address)
   {
      addHeader(new Header(MailHeader.DELIVERY_RECIEPT.headerValue(), "<" + address + ">"));
   }

   public void addReadReciept(String address)
   {
      addHeader(new Header(MailHeader.READ_RECIEPT.headerValue(), "<" + address + ">"));
   }

   public void setImportance(MessagePriority messagePriority)
   {
      if (messagePriority != null)
      {
         setHeader(new Header("X-Priority", messagePriority.getX_priority()));
         setHeader(new Header("Priority", messagePriority.getPriority()));
         setHeader(new Header("Importance", messagePriority.getImportance()));
      }
   }

   public void setHeader(Header header)
   {
      try
      {
         rootMimeMessage.setHeader(header.getName(), header.getValue());
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to SET Header: + " + header.getName() + " to Value: " + header.getValue(), e);
      }
   }

   public void addHeader(Header header)
   {
      try
      {
         rootMimeMessage.addHeader(header.getName(), header.getValue());
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to ADD Header: + " + header.getName() + " to Value: " + header.getValue(), e);
      }
   }

   public void setText(String text)
   {
      try
      {
         rootMultipart.addBodyPart(buildTextBodyPart(text));
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to add TextBody to MimeMessage", e);
      }
   }

   public void setHTML(String html)
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
         throw new RuntimeException("Unable to add TextBody to MimeMessage", e);
      }
   }

   public void setHTMLTextAlt(String html, String text)
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
         throw new RuntimeException("Unable to build HTML+Text Email", e);
      }
   }

   public void setCalendar(String body, AttachmentPart invite)
   {
      MimeBodyPart calendarBodyPart = buildHTMLBodyPart(body);
      try
      {
         rootMultipart.addBodyPart(calendarBodyPart);
         rootMultipart.addBodyPart(invite);
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to add Calendar Body to MimeMessage", e);
      }
   }

   private MimeBodyPart buildTextBodyPart(String text)
   {
      MimeBodyPart textBodyPart = new MimeBodyPart();

      try
      {
         textBodyPart.setDisposition(ContentDisposition.INLINE.headerValue());
         textBodyPart.setText(text, charset);
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to build TextBodyPart", e);
      }

      return textBodyPart;
   }

   private MimeBodyPart buildHTMLBodyPart(String html)
   {
      MimeBodyPart htmlBodyPart = new MimeBodyPart();

      try
      {
         htmlBodyPart.setDisposition(ContentDisposition.INLINE.headerValue());
         htmlBodyPart.setText(html, charset, "html");
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to build HTMLBodyPart", e);
      }

      return htmlBodyPart;
   }

   public void addAttachment(EmailAttachment emailAttachment)
   {
      AttachmentPart attachment = new AttachmentPart(emailAttachment.getBytes(), emailAttachment.getUid(), emailAttachment.getFileName(), emailAttachment.getMimeType(), emailAttachment.getHeaders(), emailAttachment.getContentDisposition());
      attachments.put(attachment.getAttachmentFileName(), attachment);
   }
   
   public void addAttachments(Collection<EmailAttachment> emailAttachments)
   {
      for(EmailAttachment ea : emailAttachments)
      {
         addAttachment(ea);
      }
   }

   public Map<String, AttachmentPart> getAttachments()
   {
      return attachments;
   }

   public MimeMessage getRootMimeMessage()
   {
      return rootMimeMessage;
   }

   public void finalizeMessage()
   {
      addAttachmentsToMessage();
   }

   public MimeMessage getFinalizedMessage()
   {
      finalizeMessage();
      return getRootMimeMessage();
   }

   public void send()
   {
      try
      {
         finalizeMessage();
         Transport.send(rootMimeMessage);
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Message Send Failed!", e);
      }
   }

   private void addAttachmentsToMessage()
   {
      for (AttachmentPart a : attachments.values())
      {
         if (a.getContentDisposition() == ContentDisposition.ATTACHMENT)
         {
            try
            {
               rootMultipart.addBodyPart(a);
            }
            catch (MessagingException e)
            {
               throw new RuntimeException("Unable to Add STANDARD Attachment: " + a.getAttachmentFileName(), e);
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
               throw new RuntimeException("Unable to Add INLINE Attachment: " + a.getAttachmentFileName(), e);
            }
         }
         else
         {
            throw new RuntimeException("Unsupported Attachment Content Disposition");
         }
      }
   }
}
