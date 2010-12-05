package org.jboss.seam.mail.templating;

import java.io.File;
import java.net.URL;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;

/**
 * Interface for creating email messages using a templating engine.
 * 
 * @author Cody Lerum
 */
public interface VelocityMailMessage 
{

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

   /**
    * Convenience method to set the REPLY-TO address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com"
    */
   public VelocityMailMessage replyTo(String address);

   /**
    * Convenience method to set the REPLY-TO name and address using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    */
   public VelocityMailMessage replyTo(String name, String address);

   /**
    * Convenience method to add a TO recipient using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    */
   public VelocityMailMessage to(String name, String address);

   /**
    * Convenience method to add a CC (Carbon Copy) recipient using UTF-8 charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * 
    */
   public VelocityMailMessage cc(String name, String address);

   /**
    * Convenience method to add a BCC (Blind Carbon Copy) recipient using UTF-8
    * charset
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * 
    */
   public VelocityMailMessage bcc(String name, String address);

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

   /**
    * Sets the importance level of the message with a given
    * {@link MessagePriority}
    * 
    * @param messagePriority The priority level of the message.
    * 
    */
   public VelocityMailMessage importance(MessagePriority messagePriority);

   /**
    * Add a given {@link File} with a given {@link ContentDisposition}
    * 
    * @param fileName Full path to the file
    * @param contentDisposition Disposition of the attachment
    * 
    */
   public VelocityMailMessage addAttachment(File fileName, ContentDisposition contentDisposition);

   /**
    * Add a file via the fileName. The classpath is searched for the specified
    * fileName and it is added to the message with the mimeType of
    * "application/octetStream" and a given {@link ContentDisposition}
    * 
    * @param fileName Name of the file to be resolved on the classpath.
    * @param contentDisposition Disposition of the attachment
    * 
    */
   public VelocityMailMessage addAttachment(String fileName, ContentDisposition contentDisposition);

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

   /**
    * Request a delivery reciept "Return-Receipt-To" to the given address
    * 
    * @param address Email address the recipent should be sent to
    * 
    */
   public VelocityMailMessage deliveryReciept(String address);

   /**
    * Request a read reciept "Disposition-Notification-To" to a given address
    * 
    * @param address Email address the recipent should be sent to
    * 
    */
   public VelocityMailMessage readReciept(String address);

   /**
    * Send the Message
    */
   public void send();

   /**
    * Sets the body of the message to the plain text output of the given
    * template
    * 
    * @param textTemplateFile File of the template classpath
    * @throws SeamMailException
    */
   public VelocityMailMessage setTemplateText(File textTemplateFile);

   /**
    * Sets the body of the message to the HTML output of the given template
    * 
    * @param htmlTemplateFile File of the template
    * 
    * @throws SeamMailException
    */
   public VelocityMailMessage setTemplateHTML(File htmlTemplateFile);

   /**
    * Sets the body of the message to a HTML body with a plain text alternative
    * output of the given templates
    * 
    * @param htmlTemplateFile File of the template
    * @param textTemplateFile File of the template
    * @throws SeamMailException
    */
   public VelocityMailMessage setTemplateHTMLTextAlt(File htmlTemplateFile, File textTemplateFile);

   /**
    * Sets the body of the message to the plain text output of the given
    * template
    * 
    * @param templateFileName Filename of the template to be found in the
    *           classpath
    * @throws SeamMailException
    */
   public VelocityMailMessage setTemplateText(String templateFileName);

   /**
    * Sets the body of the message to the HTML output of the given template
    * 
    * @param templateFileName Filename of the template to be found in the
    *           classpath
    * @throws SeamMailException
    */
   public VelocityMailMessage setTemplateHTML(String templateFileName);

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
   public VelocityMailMessage setTemplateHTMLTextAlt(String htmlTemplateFileName, String textTemplateFileName);

   /**
    * Places a variable in the templating engines context
    * 
    * @param name Reference name of the object
    * @param value the Object being placed in the context
    */
   public VelocityMailMessage put(String name, Object value);

}
