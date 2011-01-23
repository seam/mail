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

package org.jboss.seam.mail.templating;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.core.EmailAttachment;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.InvalidAddressException;
import org.jboss.seam.mail.core.SendFailedException;
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
    * Convenience method to add a FROM address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public VelocityMailMessage from(String address);

   /**
    * Convenience method to add a FROM address
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public VelocityMailMessage from(String name, String address);

   /**
    * Adds a From Address
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public VelocityMailMessage from(InternetAddress emailAddress);

   /**
    * Adds a Collection of {@link InternetAddress} as FROM addresses
    * 
    * @param emailAddresses Collection of {@link InternetAddress} to be added
    */
   public VelocityMailMessage from(Collection<InternetAddress> emailAddresses);

   /**
    * Convenience method to add a REPLY-TO address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com
    * @throws InvalidAddressException if address is in invalid format"
    */
   public VelocityMailMessage replyTo(String address);

   /**
    * Convenience method to add a REPLY-TO name and address
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public VelocityMailMessage replyTo(String name, String address);

   /**
    * Adds a REPLY-TO Address
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public VelocityMailMessage replyTo(InternetAddress emailAddress);

   /**
    * Adds a Collection of {@link InternetAddress} as REPLY-TO addresses
    * 
    * @param emailAddresses Collection of {@link InternetAddress} to be added
    */
   public VelocityMailMessage replyTo(Collection<InternetAddress> emailAddresses);

   /**
    * Convenience method to add a TO address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public VelocityMailMessage to(String address);

   /**
    * Convenience method to add a TO recipient
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public VelocityMailMessage to(String name, String address);

   /**
    * Add TO recipient
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public VelocityMailMessage to(InternetAddress emailAddress);

   /**
    * Convenience method to add a TO recipients using a collection of InternetAddress
    * 
    * @param emailAddresses Collection of {@link InternetAddress} to be added
    */
   public VelocityMailMessage to(Collection<InternetAddress> emailAddresses);

   /**
    * Convenience method to add a CC (Carbon Copy) recipient
    * 
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    * 
    */
   public VelocityMailMessage cc(String address);

   /**
    * Convenience method to add a CC (Carbon Copy) recipient
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public VelocityMailMessage cc(String name, String address);

   /**
    * Add CC (Carbon Copy) recipient
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public VelocityMailMessage cc(InternetAddress emailAddress);

   /**
    * Add collection of CC (Carbon Copy) recipients
    * 
    * @param emailAddresses Collection of {@link InternetAddress} to be added
    */
   public VelocityMailMessage cc(Collection<InternetAddress> emailAddresses);

   /**
    * Convenience method to add a BCC (Blind Carbon Copy) recipient
    * 
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public VelocityMailMessage bcc(String address);

   /**
    * Convenience method to add a BCC (Blind Carbon Copy) recipient
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public VelocityMailMessage bcc(String name, String address);

   /**
    * Add BCC (Blind Carbon Copy) recipient
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public VelocityMailMessage bcc(InternetAddress emailAddress);

   /**
    * Add collection of BCC (Blind Carbon Copy) recipients
    * 
    * @param emailAddresses Collection of {@link InternetAddress} to be added
    */
   public VelocityMailMessage bcc(Collection<InternetAddress> emailAddresses);

   // End Recipients

   // Begin Attachments

   /**
    * Add a given {@link File} with a given {@link ContentDisposition}
    * 
    * @param fileName Full path to the file
    * @param contentDisposition Disposition of the attachment
    * 
    */
   public VelocityMailMessage addAttachment(File fileName, ContentDisposition contentDisposition);

   /**
    * Add a file via the fileName. The classpath is searched for the specified fileName and it is added to the message with a given mimeType and a given
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
    * @param url {@link URL} where the file can be found
    * @param fileName Name which the attachment should be called
    * @param contentDisposition Disposition of the attachment
    * 
    */
   public VelocityMailMessage addAttachment(URL url, String fileName, ContentDisposition contentDisposition);

   /**
    * Adds Attachment to the message with given {@link ContentDisposition}
    * 
    * @param bytes Data of the file
    * @param fileName Name which the attachment should be called
    * @param mimeType MimeType of the file eg "application/octetStream"
    * @param contentDisposition Disposition of the attachment
    */
   public VelocityMailMessage addAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition);

   /**
    * Adds Attachment to the message
    * 
    * @param attachment {@link EmailAttachment} to be added
    */
   public VelocityMailMessage addAttachment(EmailAttachment attachment);

   // End Attachements

   // Begin Flags

   /**
    * Sets the importance level of the message with a given {@link MessagePriority}
    * 
    * @param messagePriority The priority level of the message.
    * 
    */
   public VelocityMailMessage importance(MessagePriority messagePriority);

   /**
    * Request a delivery receipt "Return-Receipt-To" to the given address
    * 
    * @param address Email address the receipt should be sent to
    * @throws InvalidAddressException if address is in invalid format
    */
   public VelocityMailMessage deliveryReceipt(String address);

   /**
    * Request a read receipt "Disposition-Notification-To" to a given address
    * 
    * @param address Email address the receipt should be sent to
    * @throws InvalidAddressException if address is in invalid format
    */
   public VelocityMailMessage readReceipt(String address);

   /**
    * Set the Message-ID for the message.
    * 
    * @param messageId
    */
   public VelocityMailMessage messageId(String messageId);

   // End Flags

   // Begin Calendar

   /**
    * Used for creating iCal Calendar Invites.
    * 
    * @param htmlSummary Summary of the invite to be displayed in the body of the email messages.
    * @param bytes iCal data which will be attached to the message
    * 
    */
   public VelocityMailMessage iCal(String htmlSummary, byte[] bytes);

   // End Calendar

   // Begin Core

   /**
    * Set the subject on the message
    * 
    * @param value Subject of the message
    * 
    */
   public VelocityMailMessage subject(String value);

   /**
    * Sets the body of the message a plan text body represented by the supplied string
    * 
    * @param text Plain text body
    * 
    */
   public VelocityMailMessage textBody(String text);

   /**
    * Sets the body of the message a HTML body represented by the supplied string
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

   // End Core

   /**
    * Get the {@link EmailMessage} representing this {@link VelocityMailMessage}
    * 
    * @return {@link EmailMessage} representing this {@link VelocityMailMessage}
    */
   public EmailMessage getEmailMessage();

   /**
    * Send the Message
    * 
    * @return {@link EmailMessage} which represents the {@link VelocityMailMessage} as sent
    * @throws TemplatingException - If errors occur during template processing
    * @throws SendFailedException If the messages fails to be sent.
    */
   public EmailMessage send(Session session);

   // Begin Velocity Specific

   /**
    * Set the template to be used for the message subject
    * 
    * @param text Template text to be used
    * @throws TemplatingException
    */
   public VelocityMailMessage templateSubject(String text);

   /**
    * Set the template text to be used for the plain text body of the message
    * 
    * @param text Template text to be used
    * @throws TemplatingException
    */
   public VelocityMailMessage templateText(String text);

   /**
    * Set the template text to be used for the HTML body of the message
    * 
    * @param html Template text to be used
    * @throws TemplatingException
    */
   public VelocityMailMessage templateHTML(String html);

   /**
    * Set the template text to be used for the HTML body and plain text alternative body
    * 
    * @param html Template text to be used for HTML body
    * @param text Template text to be used plain text alternative
    * @throws TemplatingException
    */
   public VelocityMailMessage templateHTMLTextAlt(String html, String text);

   /**
    * Set the template to be used for the message subject
    * 
    * @param file {@link File} of the template
    * @throws TemplatingException
    */
   public VelocityMailMessage templateSubject(File file);

   /**
    * Sets the text body of the message to the plain text output of the given template
    * 
    * @param file {@link File} of the template
    * @throws TemplatingException
    */
   public VelocityMailMessage templateText(File file);

   /**
    * Sets the HTML body of the message to the HTML output of the given template
    * 
    * @param file {@link File} of the template
    * @throws TemplatingException
    */
   public VelocityMailMessage templateHTML(File file);

   /**
    * Sets the body of the message to a HTML body with a plain text alternative output of the given templates
    * 
    * @param htmlFile {@link File} of the template for HTML body part
    * @param textFile {@link File} of the template for Text body part
    * @throws TemplatingException
    */
   public VelocityMailMessage templateHTMLTextAlt(File htmlFile, File textFile);

   /**
    * Sets the subject of the message to the plain text output of the given template
    * 
    * @param templateFileName Filename of the template to be found in the classpath
    * @throws TemplatingException
    */
   public VelocityMailMessage templateSubjectFromClassPath(String templateFileName);

   /**
    * Sets the body of the message to the plain text output of the given template
    * 
    * @param templateFileName Filename of the template to be found in the classpath
    * @throws TemplatingException
    */
   public VelocityMailMessage templateTextFromClassPath(String templateFileName);

   /**
    * Sets the body of the message to the HTML output of the given template
    * 
    * @param templateFileName Filename of the template to be found in the classpath
    * @throws TemplatingException
    */
   public VelocityMailMessage templateHTMLFromClassPath(String templateFileName);

   /**
    * Sets the body of the message to a HTML body with a plain text alternative output of the given templates
    * 
    * @param htmlTemplateFileName Filename of the template to be found in the classpath
    * @param textTemplateFileName Filename of the template to be found in the classpath
    * @throws TemplatingException
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
