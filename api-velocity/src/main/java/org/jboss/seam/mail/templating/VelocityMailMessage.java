package org.jboss.seam.mail.templating;

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
 * Interface for creating email messages using a templating engine.
 * 
 * @author Cody Lerum
 */
public interface VelocityMailMessage
{

   // Begin Recipients

   /**
    * Convenience method to set the FROM address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com"
    */
   public VelocityMailMessage from(String address);

   /**
    * Convenience method to set the FROM name and address using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    */
   public VelocityMailMessage from(String name, String address);

   public VelocityMailMessage from(EmailContact emailContact);

   /**
    * Convenience method to set the REPLY-TO address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com"
    */
   public VelocityMailMessage replyTo(String address);

   /**
    * Convenience method to set the REPLY-TO name and address using UTF-8
    * charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    */
   public VelocityMailMessage replyTo(String name, String address);

   public VelocityMailMessage to(String address);

   /**
    * Convenience method to add a TO recipient using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    */
   public VelocityMailMessage to(String name, String address);

   /**
    * Add TO recipient using EmailContact
    * 
    * @param emailContact
    * @return
    */
   public VelocityMailMessage to(EmailContact emailContact);

   /**
    * Convenience method to add a TO recipients using a collection of
    * EmailContact
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    */
   public VelocityMailMessage to(Collection<EmailContact> emailContacts);

   /**
    * Convenience method to add a CC (Carbon Copy) recipient using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * 
    */
   public VelocityMailMessage cc(String address);

   /**
    * Convenience method to add a CC (Carbon Copy) recipient using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * 
    */
   public VelocityMailMessage cc(String name, String address);

   public VelocityMailMessage cc(EmailContact emailContact);

   /**
    * Add collection of CC recipients
    * 
    * @param emailContact Collection of EmailContact
    * @return
    */
   public VelocityMailMessage cc(Collection<EmailContact> emailContacts);

   public VelocityMailMessage bcc(String address);

   /**
    * Convenience method to add a BCC (Blind Carbon Copy) recipient using UTF-8
    * charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * 
    */
   public VelocityMailMessage bcc(String name, String address);

   public VelocityMailMessage bcc(EmailContact emailContact);

   public VelocityMailMessage bcc(Collection<EmailContact> emailContacts);

   // End Recipients

   /**
    * Set the subject on the message
    * 
    * @param value Subject of the message
    * 
    */
   public VelocityMailMessage subject(String value);

   /**
    * Sets the body of the message a plan text body represented by the supplied
    * string
    * 
    * @param text Plain text body
    * 
    */
   public VelocityMailMessage textBody(String text);

   /**
    * Sets the body of the message a HTML body represented by the supplied
    * string
    * 
    * @param html HTML body
    * 
    */
   public VelocityMailMessage htmlBody(String html);

   /**
    * Sets the body of the message to a HTML body with a plain text alternative
    * 
    * @param html HTML body
    * @param text Plain text body
    * 
    */
   public VelocityMailMessage htmlBodyTextAlt(String html, String text);

   // Begin Attachments 

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
   public VelocityMailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDisposition);

   /**
    * Adds a file to the message which can be found at the given {@link URL}
    * 
    * @param url URL where the file can be found
    * @param fileName Name which the attachment should be called
    * @param contentDisposition Disposition of the attachment
    * 
    */
   public VelocityMailMessage addAttachment(URL url, String fileName, ContentDisposition contentDisposition);

   public VelocityMailMessage addAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition);

   public VelocityMailMessage addAttachment(EmailAttachment attachment);

   // End Attachements

   // Begin Calendar

   /**
    * Calendar invites require a special format.
    * 
    * @param htmlSummary Summary of the invite to be displayed in the message
    * @param bytes Calendar data
    * 
    */
   public VelocityMailMessage iCal(String htmlSummary, byte[] bytes);

   // End Calendar

   // Begin Flags

   /**
    * Sets the importance level of the message with a given
    * {@link MessagePriority}
    * 
    * @param messagePriority The priority level of the message.
    * 
    */
   public VelocityMailMessage importance(MessagePriority messagePriority);

   /**
    * Request a delivery receipt "Return-Receipt-To" to the given address
    * 
    * @param address Email address the receipt should be sent to
    * 
    */
   public VelocityMailMessage deliveryReceipt(String address);

   /**
    * Request a read receipt "Disposition-Notification-To" to a given address
    * 
    * @param address Email address the receipt should be sent to
    * 
    */
   public VelocityMailMessage readReceipt(String address);
   
   /**
    * Set the Message-ID for the message. Will only be used once. Attempts to send message again with same id will fail.
    * @param messageId
    * @return
    */
   public VelocityMailMessage messageId(String messageId);

   // End Flags
   
   public EmailMessage getEmailMessage();
   
   public VelocityMailMessage mergeTemplates();

   /**
    * Send the Message
    * @throws SendFailedException 
    */
   public EmailMessage send(Session session) throws SendFailedException;

   public VelocityMailMessage templateSubject(String text);
   
   public VelocityMailMessage templateText(String text);
   
   public VelocityMailMessage templateHTML(String html);
   
   public VelocityMailMessage templateHTMLTextAlt(String html, String text);
   
   
   public VelocityMailMessage templateSubject(File file);
   
   /**
    * Sets the body of the message to the plain text output of the given
    * template
    * 
    * @param file File of the template classpath
    * @throws SeamMailException
    */
   public VelocityMailMessage templateText(File file);

   /**
    * Sets the body of the message to the HTML output of the given template
    * 
    * @param file File of the template
    * 
    * @throws SeamMailException
    */
   public VelocityMailMessage templateHTML(File file);

   /**
    * Sets the body of the message to a HTML body with a plain text alternative
    * output of the given templates
    * 
    * @param htmlFile File of the template for HTML body part
    * @param textFile File of the template for Text body part
    * @throws SeamMailException
    */
   public VelocityMailMessage templateHTMLTextAlt(File htmlFile, File textFile);

   public VelocityMailMessage templateSubjectFromClassPath(String fileName);
   
   /**
    * Sets the body of the message to the plain text output of the given
    * template
    * 
    * @param templateFileName Filename of the template to be found in the
    *           classpath
    * @throws SeamMailException
    */
   public VelocityMailMessage templateTextFromClassPath(String templateFileName);

   /**
    * Sets the body of the message to the HTML output of the given template
    * 
    * @param templateFileName Filename of the template to be found in the
    *           classpath
    * @throws SeamMailException
    */
   public VelocityMailMessage templateHTMLFromClassPath(String templateFileName);

   /**
    * Sets the body of the message to a HTML body with a plain text alternative
    * output of the given templates
    * 
    * @param htmlTemplateFileName Filename of the template to be found in the
    *           classpath
    * @param textTemplateFileName Filename of the template to be found in the
    *           classpath
    * @throws SeamMailException
    */
   public VelocityMailMessage templateHTMLTextAltFromClassPath(String htmlTemplateFileName, String textTemplateFileName);

   /**
    * Places a variable in the templating engines context
    * 
    * @param name Reference name of the object
    * @param value the Object being placed in the context
    */
   public VelocityMailMessage put(String name, Object value);
}
