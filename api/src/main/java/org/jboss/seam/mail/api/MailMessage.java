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
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.core.EmailAttachment;
import org.jboss.seam.mail.core.EmailContact;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.InvalidAddressException;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.MailTransporter;
import org.jboss.seam.mail.core.SendFailedException;
import org.jboss.seam.mail.core.TemplatingException;
import org.jboss.seam.mail.core.enumerations.ContentDisposition;
import org.jboss.seam.mail.core.enumerations.ContentType;
import org.jboss.seam.mail.core.enumerations.MessagePriority;
import org.jboss.seam.mail.templating.TemplateProvider;

/**
 * Base interface for creating email messages.
 * 
 * @author Cody Lerum
 */
public interface MailMessage {

    // Begin Recipients

    /**
     * Convenience varargs method to add FROM address(es)
     * 
     * @param address Address of the recipient eq "john.doe@example.com" or "John Doe<john.doe@example.com>"
     * @throws InvalidAddressException if address(es) are in an invalid format
     */
    public MailMessage from(String... address);

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
    public MailMessage from(Collection<? extends EmailContact> emailContacts);

    /**
     * Convenience varargs method to add REPLY-TO address(es)
     * 
     * @param address Address of the recipient eq "john.doe@example.com" or "John Doe<john.doe@example.com>"
     * @throws InvalidAddressException if address(es) are in an invalid format
     */
    public MailMessage replyTo(String... address);

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
    public MailMessage replyTo(Collection<? extends EmailContact> emailContacts);

    /**
     * Add header to the message.
     * 
     * @param name Header name
     * @param value Header value
     */
    public MailMessage addHeader(String name, String value);

    /**
     * Convenience varargs method to add TO address(es)
     * 
     * @param address Address of the recipient eq "john.doe@example.com" or "John Doe<john.doe@example.com>"
     * @throws InvalidAddressException if address(es) are in an invalid format
     */
    public MailMessage to(String... address);

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
    public MailMessage to(Collection<? extends EmailContact> emailContacts);

    /**
     * Convenience varargs method to add CC address(es)
     * 
     * @param address Address of the recipient eq "john.doe@example.com" or "John Doe<john.doe@example.com>"
     * @throws InvalidAddressException if address(es) are in an invalid format
     */
    public MailMessage cc(String... address);

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
    public MailMessage cc(Collection<? extends EmailContact> emailContacts);

    /**
     * Convenience varargs method to add BCC address(es)
     * 
     * @param address Address of the recipient eq "john.doe@example.com" or "John Doe<john.doe@example.com>"
     * @throws InvalidAddressException if address(es) are in an invalid format
     */
    public MailMessage bcc(String... address);

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
    public MailMessage bcc(Collection<? extends EmailContact> emailContacts);

    // End Recipients

    // Begin Attachments

    /**
     * Adds Attachment to the message
     * 
     * @param attachment {@link EmailAttachment} to be added
     */
    public MailMessage addAttachment(EmailAttachment attachment);

    /**
     * Adds a Collection of Attachments to the message
     * 
     * @param attachments
     */
    public MailMessage addAttachments(Collection<? extends EmailAttachment> attachments);

    /**
     * Adds Attachment to the message
     * 
     * @param fileName
     * @param mimeType
     * @param contentDispostion
     * @param bytes
     */
    public MailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDispostion, byte[] bytes);

    /**
     * Adds Attachment to the message
     * 
     * @param fileName
     * @param mimeType
     * @param contentDispostion
     * @param inputStream
     */
    public MailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDispostion,
            InputStream inputStream);

    /**
     * Adds Attachment to the message
     * 
     * @param contentDispostion
     * @param file
     */
    public MailMessage addAttachment(ContentDisposition contentDispostion, File file);

    // End Attachements

    // Begin Flags

    /**
     * Sets the importance level of the message with a given {@link MessagePriority}
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
     * @param htmlSummary Summary of the invite to be displayed in the body of the email messages.
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
     * Sets the body of the message a plan text body represented by the supplied string
     * 
     * @param text Plain text body
     * 
     */
    public MailMessage bodyText(String text);

    /**
     * Sets the body of the message a HTML body represented by the supplied string
     * 
     * @param html HTML body
     * 
     */
    public MailMessage bodyHtml(String html);

    /**
     * Sets the body of the message to a HTML body with a plain text alternative
     * 
     * @param html HTML body
     * @param text Plain text body
     * 
     */
    public MailMessage bodyHtmlTextAlt(String html, String text);

    /**
     * Set the charset of the message. Otherwise defaults to Charset.defaultCharset()
     * 
     * @param contentType
     */
    public MailMessage charset(String charset);

    /**
     * Set the Content Type of the message
     * 
     * @param contentType
     */
    public MailMessage contentType(ContentType contentType);

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
     * Optionally set a MailTransporter will will be used with the default send() method
     * 
     * @param mailTransporter
     */
    public void setMailTransporter(MailTransporter mailTransporter);

    /**
     * Merge the templates with the context
     * 
     * @return {@link EmailMessage} representing this {@link MailMessage} after merging
     */
    public EmailMessage mergeTemplates();

    /**
     * Send the Message
     * 
     * @param mailTransporter {@link MailTransporter} instance to used to send the {@link MailMessage}
     * @return {@link EmailMessage} which represents the {@link MailMessage} as sent
     * @throws SendFailedException If the messages fails to be sent.
     */
    public EmailMessage send(MailTransporter mailTransporter);

    /**
     * Send the Message using a specific JavaMail session
     * 
     * @param session {@link Session} to use to send the {@link MailMessage}
     * @return {@link EmailMessage} which represents the {@link MailMessage} as sent
     * @throws SendFailedException If the messages fails to be sent.
     */
    public EmailMessage send(Session session);

    /**
     * Send the Message using a JavaMail session created from this specific MailConfig
     * 
     * @param mailConfig {@link MailConfig} to use to send the {@link MailMessage}
     * @return {@link EmailMessage} which represents the {@link MailMessage} as sent
     * @throws SendFailedException If the messages fails to be sent.
     */
    public EmailMessage send(MailConfig mailConfig);

    /**
     * Send the Message using a default mail session created from a configured MailConfig
     * 
     * @return {@link EmailMessage} which represents the {@link MailMessage} as sent
     * @throws SendFailedException If the messages fails to be sent.
     */
    public EmailMessage send();

    // Templating Specific

    /**
     * Set the template to be used for the message subject
     * 
     * @param subject {@link TemplateProvider} to use
     * @throws TemplatingException
     */
    public MailMessage subject(TemplateProvider subject);

    /**
     * Sets the text body of the message to the plain text output of the given template
     * 
     * @param textBody {@link TemplateProvider} to use
     * @throws TemplatingException
     */
    public MailMessage bodyText(TemplateProvider textBody);

    /**
     * Sets the HTML body of the message to the HTML output of the given template
     * 
     * @param htmlBody {@link TemplateProvider} to use
     * @throws TemplatingException
     */
    public MailMessage bodyHtml(TemplateProvider htmlBody);

    /**
     * Sets the body of the message to a HTML body with a plain text alternative output of the given templates
     * 
     * @param htmlBody {@link TemplateProvider} to use for HTML portion of message
     * @param textBody {@link TemplateProvider} to use for Text alternative portion of message
     * @throws TemplatingException
     */
    public MailMessage bodyHtmlTextAlt(TemplateProvider htmlBody, TemplateProvider textBody);

    /**
     * Places a variable in the templating engines context
     * 
     * @param name Reference name of the object
     * @param value the Object being placed in the context
     */
    public MailMessage put(String name, Object value);

    /**
     * Places a Map of variable in the templating engines context
     * 
     * @param values Map<String, Object> containing the variables to be placed in the context
     */
    public MailMessage put(Map<String, Object> values);
}
