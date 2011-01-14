package org.jboss.seam.mail.api;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import javax.mail.SendFailedException;
import javax.mail.Session;

import org.jboss.seam.mail.core.EmailAttachment;
import org.jboss.seam.mail.core.EmailContact;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;

/**
 * Base interface for creating email messages.
 * 
 * @author Cody Lerum
 */
public interface MailMessage
{

   // Begin Recipients

   /**
    * Convenience method to set the FROM address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com"
    */
   public MailMessage from(String address);

   /**
    * Convenience method to set the FROM name and address using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    */
   public MailMessage from(String name, String address);

   public MailMessage from(EmailContact emailContact);

   /**
    * Convenience method to set the REPLY-TO address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com"
    */
   public MailMessage replyTo(String address);

   /**
    * Convenience method to set the REPLY-TO name and address using UTF-8
    * charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    */
   public MailMessage replyTo(String name, String address);

   public MailMessage to(String address);

   /**
    * Convenience method to add a TO recipient using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    */
   public MailMessage to(String name, String address);

   /**
    * Add TO recipient using EmailContact
    * 
    * @param emailContact
    * @return
    */
   public MailMessage to(EmailContact emailContact);

   /**
    * Convenience method to add a TO recipients using a collection of
    * EmailContact
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    */
   public MailMessage to(Collection<EmailContact> emailContacts);

   /**
    * Convenience method to add a CC (Carbon Copy) recipient using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * 
    */
   public MailMessage cc(String address);

   /**
    * Convenience method to add a CC (Carbon Copy) recipient using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * 
    */
   public MailMessage cc(String name, String address);

   public MailMessage cc(EmailContact emailContact);

   /**
    * Add collection of CC recipients
    * 
    * @param emailContact Collection of EmailContact
    * @return
    */
   public MailMessage cc(Collection<EmailContact> emailContacts);

   public MailMessage bcc(String address);

   /**
    * Convenience method to add a BCC (Blind Carbon Copy) recipient using UTF-8
    * charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * 
    */
   public MailMessage bcc(String name, String address);

   public MailMessage bcc(EmailContact emailContact);

   public MailMessage bcc(Collection<EmailContact> emailContacts);

   // End Recipients

   // Begin Attachments

   /**
    * Add a given {@link File} with a given {@link ContentDisposition}
    * 
    * @param fileName Full path to the file
    * @param contentDisposition Disposition of the attachment
    * 
    */
   public MailMessage addAttachment(File fileName, ContentDisposition contentDisposition);

   /**
    * Add a file via the fileName. The classpath is searched for the specified
    * fileName and it is added to the message with a given mimeType and a given
    * {@link ContentDisposition}
    * 
    * @param fileName Name of the file to be attached.
    * @param mimeType MimeType of the file eg "application/octetStream"
    * @param contentDisposition Disposition of the attachment
    * 
    */
   public MailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDisposition);

   /**
    * Adds a file to the message which can be found at the given {@link URL}
    * 
    * @param url URL where the file can be found
    * @param fileName Name which the attachment should be called
    * @param contentDisposition Disposition of the attachment
    * 
    */
   public MailMessage addAttachment(URL url, String fileName, ContentDisposition contentDisposition);

   public MailMessage addAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition);

   public MailMessage addAttachment(EmailAttachment attachment);

   // End Attachements

   // Begin Flags

   /**
    * Sets the importance level of the message with a given
    * {@link MessagePriority}
    * 
    * @param messagePriority The priority level of the message.
    * 
    */
   public MailMessage importance(MessagePriority messagePriority);

   /**
    * Request a delivery receipt "Return-Receipt-To" to the given address
    * 
    * @param address Email address the receipt should be sent to
    * 
    */
   public MailMessage deliveryReceipt(String address);

   /**
    * Request a read receipt "Disposition-Notification-To" to a given address
    * 
    * @param address Email address the receipt should be sent to
    * 
    */
   public MailMessage readReceipt(String address);
   
   /**
    * Set the Message-ID for the message. Will only be used once. Attempts to send message again with same id will fail.
    * @param messageId
    * @return
    */
   public MailMessage messageId(String messageId);

   // End Flags

   // Begin Calendar

   /**
    * Calendar invites require a special format.
    * 
    * @param htmlSummary Summary of the invite to be displayed in the message
    * @param bytes Calendar data
    * 
    */
   public MailMessage iCal(String htmlSummary, byte[] bytes);

   // End Calendar

   // Begin Core

   /**
    * Set the subject on the message
    * 
    * @param value Subject of the message
    * 
    */
   public MailMessage subject(String value);

   /**
    * Sets the body of the message a plan text body represented by the supplied
    * string
    * 
    * @param text Plain text body
    * 
    */
   public MailMessage textBody(String text);

   /**
    * Sets the body of the message a HTML body represented by the supplied
    * string
    * 
    * @param html HTML body
    * 
    */
   public MailMessage htmlBody(String html);

   /**
    * Sets the body of the message to a HTML body with a plain text alternative
    * 
    * @param html HTML body
    * @param text Plain text body
    * 
    */
   public MailMessage htmlBodyTextAlt(String html, String text);

   // End Core

   public EmailMessage getEmailMessage();
   
   /**
    * Send the Message
    * 
    * @return
    * @throws SendFailedException 
    */
   public EmailMessage send(Session session) throws SendFailedException;

}
