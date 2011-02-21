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

package org.jboss.seam.mail.api;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.core.EmailAttachment;
import org.jboss.seam.mail.core.EmailContact;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.InvalidAddressException;
import org.jboss.seam.mail.core.SendFailedException;
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
    * Convenience method to add a FROM address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public MailMessage from(String address);

   /**
    * Convenience method to add a FROM address
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public MailMessage from(String address, String name);

   /**
    * Adds a From Address
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public MailMessage from(InternetAddress emailAddress);

   /**
    * Adds a From Address
    * 
    * @param emailContact {@link EmailContact} of the address to be added
    */
   public MailMessage from(EmailContact emailContact);

   /**
    * Adds a Collection of {@link EmailContact} as FROM addresses
    * 
    * @param emailContacts Collection of {@link EmailContact} to be added
    */
   public MailMessage from(Collection<EmailContact> emailContacts);

   /**
    * Convenience method to add a REPLY-TO address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com
    * @throws InvalidAddressException if address is in invalid format"
    */
   public MailMessage replyTo(String address);

   /**
    * Convenience method to add a REPLY-TO name and address
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public MailMessage replyTo(String address, String name);

   /**
    * Adds a REPLY-TO Address
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public MailMessage replyTo(InternetAddress emailAddress);

   /**
    * Adds a REPLY-TO Address
    * 
    * @param emailContact {@link EmailContact} of the address to be added
    */
   public MailMessage replyTo(EmailContact emailContact);

   /**
    * Adds a Collection of {@link EmailContact} as REPLY-TO addresses
    * 
    * @param emailContacts Collection of {@link EmailContact} to be added
    */
   public MailMessage replyTo(Collection<EmailContact> emailContacts);

   /**
    * Convenience method to add a TO address
    * 
    * @param address Email address of the recipient eq "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public MailMessage to(String address);

   /**
    * Convenience method to add a TO recipient
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public MailMessage to(String address, String name);

   /**
    * Add TO recipient
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public MailMessage to(InternetAddress emailAddress);

   /**
    * Add TO recipient
    * 
    * @param emailContact {@link EmailContact} of the address to be added
    */
   public MailMessage to(EmailContact emailContact);

   /**
    * Convenience method to add a TO recipients
    * 
    * @param emailContacts Collection of {@link EmailContact} to be added
    */
   public MailMessage to(Collection<EmailContact> emailContacts);

   /**
    * Convenience method to add a CC (Carbon Copy) recipient
    * 
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    * 
    */
   public MailMessage cc(String address);

   /**
    * Convenience method to add a CC (Carbon Copy) recipient
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public MailMessage cc(String address, String name);

   /**
    * Add CC (Carbon Copy) recipient
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public MailMessage cc(InternetAddress emailAddress);

   /**
    * Add CC recipient
    * 
    * @param emailContact {@link EmailContact} of the address to be added
    */
   public MailMessage cc(EmailContact emailContact);

   /**
    * Add collection of CC (Carbon Copy) recipients
    * 
    * @param emailContacts Collection of {@link EmailContact} to be added
    */
   public MailMessage cc(Collection<EmailContact> emailContacts);

   /**
    * Convenience method to add a BCC (Blind Carbon Copy) recipient
    * 
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public MailMessage bcc(String address);

   /**
    * Convenience method to add a BCC (Blind Carbon Copy) recipient
    * 
    * @param name Personal name of the recipient eg "John Doe"
    * @param address Email address of the recipient eg "john.doe@example.com"
    * @throws InvalidAddressException if address is in invalid format
    */
   public MailMessage bcc(String address, String name);

   /**
    * Add BCC (Blind Carbon Copy) recipient
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public MailMessage bcc(InternetAddress emailAddress);

   /**
    * Add BCC recipient
    * 
    * @param emailContact {@link EmailContact} of the address to be added
    */
   public MailMessage bcc(EmailContact emailContact);

   /**
    * Add collection of BCC (Blind Carbon Copy) recipients
    * 
    * @param emailContacts Collection of {@link EmailContact} to be added
    */
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
    * @param url {@link URL} where the file can be found
    * @param fileName Name which the attachment should be called
    * @param contentDisposition Disposition of the attachment
    * 
    */
   public MailMessage addAttachment(URL url, String fileName, ContentDisposition contentDisposition);

   /**
    * Adds Attachment to the message with given {@link ContentDisposition}
    * 
    * @param bytes Data of the file
    * @param fileName Name which the attachment should be called
    * @param mimeType MimeType of the file eg "application/octetStream"
    * @param contentDisposition Disposition of the attachment
    */
   public MailMessage addAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition);

   /**
    * Adds Attachment to the message
    * 
    * @param attachment {@link EmailAttachment} to be added
    */
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
    * @throws InvalidAddressException if address is in invalid format
    */
   public MailMessage deliveryReceipt(String address);

   /**
    * Request a read receipt "Disposition-Notification-To" to a given address
    * 
    * @param address Email address the receipt should be sent to
    * @throws InvalidAddressException if address is in invalid format
    */
   public MailMessage readReceipt(String address);

   /**
    * Set the Message-ID for the message.
    * 
    * @param messageId
    */
   public MailMessage messageId(String messageId);

   // End Flags

   // Begin Calendar

   /**
    * Used for creating iCal Calendar Invites.
    * 
    * @param htmlSummary Summary of the invite to be displayed in the body of
    *           the email messages.
    * @param bytes iCal data which will be attached to the message
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

   /**
    * Get the {@link EmailMessage} representing this {@link MailMessage}
    * 
    * @return {@link EmailMessage} representing this {@link MailMessage}
    */
   public EmailMessage getEmailMessage();
   
   /**
    * Set the {@link EmailMessage} representing this {@link MailMessage}
    * 
    */
   public void setEmailMessage(EmailMessage emailMessage);

   /**
    * Send the Message
    * 
    * @param session {@link Session} to use to send the {@link MailMessage}
    * @return {@link EmailMessage} which represents the {@link MailMessage} as
    *         sent
    * @throws SendFailedException If the messages fails to be sent.
    */
   public EmailMessage send(Session session);

   /**
    * Send the Message
    * 
    * @return {@link EmailMessage} which represents the {@link MailMessage} as
    *         sent
    * @throws SendFailedException If the messages fails to be sent.
    */
   public EmailMessage send();

}
