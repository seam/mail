package org.jboss.seam.mail.core;

import java.io.File;
import java.net.URL;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.core.enumurations.RecipientType;
import org.jboss.seam.mail.exception.SeamMailException;

/**
 * Base interface for creating email messages.
 * 
 * @author Cody Lerum
 */
public interface MailMessage<T extends MailMessage<T>>
{
   /**
    * Set the From address
    */
   public T setFrom(EmailContact emailContact) throws SeamMailException;

   /**
    * Add a recipient to the message
    * 
    * @param recipientType {@link RecipientType} TO/CC/BCC
    * 
    * @param emailContact {@link EmailContact} Name and Address
    */
   public T addRecipient(RecipientType recipientType, EmailContact emailContact) throws SeamMailException;

   /**
    * Convenience method to set the FROM address using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg
    *           "john.doe@example.com"
    * @throws SeamMailException
    */
   public T from(String name, String address) throws SeamMailException;

   /**
    * Convenience method to add a TO recipient using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg
    *           "john.doe@example.com"
    * @throws SeamMailException
    */
   public T to(String name, String address) throws SeamMailException;

   /**
    * Convenience method to add a CC (Carbon Copy) recipient using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg
    *           "john.doe@example.com"
    * @throws SeamMailException
    */
   public T cc(String name, String address) throws SeamMailException;

   /**
    * Convenience method to add a BCC (Blind Carbon Copy) recipient using UTF-8
    * charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg
    *           "john.doe@example.com"
    * @throws SeamMailException
    */
   public T bcc(String name, String address) throws SeamMailException;

   /**
    * Set the subject on the message
    * 
    * @param value Subject of the message
    * @throws SeamMailException
    */
   public T subject(String value) throws SeamMailException;

   /**
    * Sets the body of the message a plan text body represented by the supplied
    * string
    * 
    * @param text Plain text body
    * @throws SeamMailException
    */
   public T setText(String text) throws SeamMailException;

   /**
    * Sets the body of the message a HTML body represented by the supplied
    * string
    * 
    * @param html HTML body
    * @throws SeamMailException
    */
   public T setHTML(String html) throws SeamMailException;

   /**
    * Sets the body of the message to a HTML body with a plain text alternative
    * 
    * @param html HTML body
    * @param text Plain text body
    * @throws SeamMailException
    */
   public T setHTMLTextAlt(String html, String text) throws SeamMailException;

   /**
    * Sets the importance level of the message with a given
    * {@link MessagePriority}
    * 
    * @param messagePriority The priority level of the message.
    * @throws SeamMailException
    */
   public T importance(MessagePriority messagePriority) throws SeamMailException;

   /**
    * Add a given {@link File} with a given {@link ContentDisposition}
    * 
    * @param fileName Full path to the file
    * @param contentDisposition Disposition of the attachment
    * @throws SeamMailException
    */
   public T addAttachment(File fileName, ContentDisposition contentDisposition) throws SeamMailException;

   /**
    * Add a file via the fileName. The classpath is searched for the specified
    * fileName and it is added to the message with the mimeType of
    * "application/octetStream" and a given {@link ContentDisposition}
    * 
    * @param fileName Name of the file to be resolved on the classpath.
    * @param contentDisposition Disposition of the attachment
    * @throws SeamMailException
    */
   public T addAttachment(String fileName, ContentDisposition contentDisposition) throws SeamMailException;

   /**
    * Add a file via the fileName. The classpath is searched for the specified
    * fileName and it is added to the message with a given mimeType and a given
    * {@link ContentDisposition}
    * 
    * @param fileName Name of the file to be attached.
    * @param mimeType MimeType of the file eg "application/octetStream"
    * @param contentDisposition Disposition of the attachment
    * @throws SeamMailException
    */
   public T addAttachment(String fileName, String mimeType, ContentDisposition contentDisposition) throws SeamMailException;

   /**
    * Adds a file to the message which can be found at the given {@link URL}
    * 
    * @param url URL where the file can be found
    * @param fileName Name which the attachment should be called
    * @param contentDisposition Disposition of the attachment
    * @throws SeamMailException
    */
   public T addAttachment(URL url, String fileName, ContentDisposition contentDisposition) throws SeamMailException;

   /**
    * Request a delivery reciept "Return-Receipt-To" to the given address
    * 
    * @param address Email address the recipent should be sent to
    * @throws SeamMailException
    */
   public T deliveryReciept(String address) throws SeamMailException;

   /**
    * Request a read reciept "Disposition-Notification-To" to a given address
    * 
    * @param address Email address the recipent should be sent to
    * @throws SeamMailException
    */
   public T readReciept(String address) throws SeamMailException;

   /**
    * Send the Message
    * @throws SeamMailException
    */
   public void send() throws SeamMailException;

}
