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

import java.util.Collection;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.EmailAttachment;
import org.jboss.seam.mail.core.EmailContact;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.InvalidAddressException;
import org.jboss.seam.mail.core.MailTemplate;
import org.jboss.seam.mail.core.SendFailedException;
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
   public VelocityMailMessage from(String address, String name);

   /**
    * Adds a From Address
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public VelocityMailMessage from(InternetAddress emailAddress);

   /**
    * Adds a From Address
    * 
    * @param emailContact {@link EmailContact} of the address to be added
    */
   public VelocityMailMessage from(EmailContact emailContact);

   /**
    * Adds a Collection of {@link EmailContact} as FROM addresses
    * 
    * @param emailContacts Collection of {@link EmailContact} to be added
    */
   public VelocityMailMessage from(Collection<EmailContact> emailContacts);

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
   public VelocityMailMessage replyTo(String address, String name);

   /**
    * Adds a REPLY-TO Address
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public VelocityMailMessage replyTo(InternetAddress emailAddress);

   /**
    * Adds a REPLY-TO Address
    * 
    * @param emailContact {@link EmailContact} of the address to be added
    */
   public VelocityMailMessage replyTo(EmailContact emailContact);

   /**
    * Adds a Collection of {@link EmailContact} as REPLY-TO addresses
    * 
    * @param emailContacts Collection of {@link EmailContact} to be added
    */
   public VelocityMailMessage replyTo(Collection<EmailContact> emailContacts);

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
   public VelocityMailMessage to(String address, String name);

   /**
    * Add TO recipient
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public VelocityMailMessage to(InternetAddress emailAddress);

   /**
    * Add TO recipient
    * 
    * @param emailContact {@link EmailContact} of the address to be added
    */
   public VelocityMailMessage to(EmailContact emailContact);

   /**
    * Convenience method to add a TO recipients
    * 
    * @param emailContacts Collection of {@link EmailContact} to be added
    */
   public VelocityMailMessage to(Collection<EmailContact> emailContacts);

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
   public VelocityMailMessage cc(String address, String name);

   /**
    * Add CC (Carbon Copy) recipient
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public VelocityMailMessage cc(InternetAddress emailAddress);

   /**
    * Add CC recipient
    * 
    * @param emailContact {@link EmailContact} of the address to be added
    */
   public VelocityMailMessage cc(EmailContact emailContact);

   /**
    * Add collection of CC (Carbon Copy) recipients
    * 
    * @param emailContacts Collection of {@link EmailContact} to be added
    */
   public VelocityMailMessage cc(Collection<EmailContact> emailContacts);

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
   public VelocityMailMessage bcc(String address, String name);

   /**
    * Add BCC (Blind Carbon Copy) recipient
    * 
    * @param emailAddress {@link InternetAddress} of the address to be added
    */
   public VelocityMailMessage bcc(InternetAddress emailAddress);

   /**
    * Add BCC recipient
    * 
    * @param emailContact {@link EmailContact} of the address to be added
    */
   public VelocityMailMessage bcc(EmailContact emailContact);

   /**
    * Add collection of BCC (Blind Carbon Copy) recipients
    * 
    * @param emailContacts Collection of {@link EmailContact} to be added
    */
   public VelocityMailMessage bcc(Collection<EmailContact> emailContacts);

   // End Recipients

   // Begin Attachments  

   /**
    * Adds Attachment to the message
    * 
    * @param attachment {@link EmailAttachment} to be added
    */
   public VelocityMailMessage addAttachment(EmailAttachment attachment);

   // End Attachements

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
    * @param htmlSummary Summary of the invite to be displayed in the body of
    *           the email messages.
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
    * Sets the body of the message a plan text body represented by the supplied
    * string
    * 
    * @param text Plain text body
    * 
    */
   public VelocityMailMessage bodyText(String text);

   /**
    * Sets the body of the message a HTML body represented by the supplied
    * string
    * 
    * @param html HTML body
    * 
    */
   public VelocityMailMessage bodyHtml(String html);

   /**
    * Sets the body of the message to a HTML body with a plain text alternative
    * 
    * @param html HTML body
    * @param text Plain text body
    * 
    */
   public VelocityMailMessage bodyHtmlTextAlt(String html, String text);

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
    * @return {@link EmailMessage} which represents the
    *         {@link VelocityMailMessage} as sent
    * @throws TemplatingException - If errors occur during template processing
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

   // Begin Velocity Specific

   /**
    * Set the template to be used for the message subject
    * 
    * @param subject {@link MailTemplate} to use
    * @throws TemplatingException
    */
   public VelocityMailMessage subject(MailTemplate subject);

   /**
    * Sets the text body of the message to the plain text output of the given
    * template
    * 
    * @param textBody {@link MailTemplate} to use
    * @throws TemplatingException
    */
   public VelocityMailMessage bodyText(MailTemplate textbody);

   /**
    * Sets the HTML body of the message to the HTML output of the given template
    * 
    * @param htmlBody {@link MailTemplate} to use
    * @throws TemplatingException
    */
   public VelocityMailMessage bodyHtml(MailTemplate htmlBody);

   /**
    * Sets the body of the message to a HTML body with a plain text alternative
    * output of the given templates
    * 
    * @param htmlBody {@link MailTemplate} to use for HTML portion of
    *           message
    * @param textBody {@link MailTemplate} to use for Text alternative
    *           portion of message
    * @throws TemplatingException
    */
   public VelocityMailMessage bodyHtmlTextAlt(MailTemplate htmlBody, MailTemplate textbody);

   /**
    * Places a variable in the templating engines context  
    * 
    * @param name Reference name of the object
    * @param value the Object being placed in the context
    */
   public VelocityMailMessage put(String name, Object value);
}
