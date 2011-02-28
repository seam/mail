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

package org.jboss.seam.mail.core;

import java.util.Collection;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.ContentType;
import org.jboss.seam.mail.core.enumurations.EmailMessageType;
import org.jboss.seam.mail.core.enumurations.MessagePriority;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class MailMessageImpl implements MailMessage
{
   private EmailMessage emailMessage;

   @Inject
   private Instance<Session> session;

   public MailMessageImpl()
   {
      emailMessage = new EmailMessage();
   }

   public MailMessageImpl(ContentType rootContentType)
   {
      emailMessage = new EmailMessage(rootContentType);
   }

   // Begin Addressing

   public MailMessage from(String address)
   {
      emailMessage.addFromAddress(MailUtility.internetAddress(address));
      return this;
   }

   public MailMessage from(String address, String name)
   {
      emailMessage.addFromAddress(MailUtility.internetAddress(address, name));
      return this;
   }

   public MailMessage from(InternetAddress emailAddress)
   {
      emailMessage.addFromAddress(emailAddress);
      return this;
   }

   public MailMessage from(EmailContact emailContact)
   {
      emailMessage.addFromAddress(MailUtility.internetAddress(emailContact));
      return this;
   }

   public MailMessage from(Collection<EmailContact> emailContacts)
   {
      emailMessage.addFromAddresses(MailUtility.internetAddress(emailContacts));
      return this;
   }

   public MailMessage replyTo(String address)
   {
      emailMessage.addReplyToAddress(MailUtility.internetAddress(address));
      return this;
   }

   public MailMessage replyTo(String address, String name)
   {
      emailMessage.addReplyToAddress(MailUtility.internetAddress(address, name));
      return this;
   }

   public MailMessage replyTo(InternetAddress emailAddress)
   {
      emailMessage.addReplyToAddress(emailAddress);
      return this;
   }

   public MailMessage replyTo(EmailContact emailContact)
   {
      emailMessage.addReplyToAddress(MailUtility.internetAddress(emailContact));
      return this;
   }

   public MailMessage replyTo(Collection<EmailContact> emailContacts)
   {
      emailMessage.addReplyToAddresses(MailUtility.internetAddress(emailContacts));
      return this;
   }

   public MailMessage to(String address)
   {
      emailMessage.addToAddress(MailUtility.internetAddress(address));
      return this;
   }

   public MailMessage to(String address, String name)
   {
      emailMessage.addToAddress(MailUtility.internetAddress(address, name));
      return this;
   }

   public MailMessage to(InternetAddress emailAddress)
   {
      emailMessage.addToAddress(emailAddress);
      return this;
   }

   public MailMessage to(EmailContact emailContact)
   {
      emailMessage.addToAddress(MailUtility.internetAddress(emailContact));
      return this;
   }

   public MailMessage to(Collection<EmailContact> emailContacts)
   {
      emailMessage.addToAddresses(MailUtility.internetAddress(emailContacts));
      return this;
   }

   public MailMessage cc(String address)
   {
      emailMessage.addCcAddress(MailUtility.internetAddress(address));
      return this;
   }

   public MailMessage cc(String address, String name)
   {
      emailMessage.addCcAddress(MailUtility.internetAddress(address, name));
      return this;
   }

   public MailMessage cc(InternetAddress emailAddress)
   {
      emailMessage.addCcAddress(emailAddress);
      return this;
   }

   public MailMessage cc(EmailContact emailContact)
   {
      emailMessage.addCcAddress(MailUtility.internetAddress(emailContact));
      return this;
   }

   public MailMessage cc(Collection<EmailContact> emailContacts)
   {
      emailMessage.addCcAddresses(MailUtility.internetAddress(emailContacts));
      return this;
   }

   public MailMessage bcc(String address)
   {
      emailMessage.addBccAddress(MailUtility.internetAddress(address));
      return this;
   }

   public MailMessage bcc(String address, String name)
   {
      emailMessage.addBccAddress(MailUtility.internetAddress(address, name));
      return this;
   }

   public MailMessage bcc(InternetAddress emailAddress)
   {
      emailMessage.addBccAddress(emailAddress);
      return this;
   }

   public MailMessage bcc(EmailContact emailContact)
   {
      emailMessage.addBccAddress(MailUtility.internetAddress(emailContact));
      return this;
   }

   public MailMessage bcc(Collection<EmailContact> emailContacts)
   {
      emailMessage.addBccAddresses(MailUtility.internetAddress(emailContacts));
      return this;
   }

   // End Addressing

   public MailMessage subject(String value)
   {
      emailMessage.setSubject(value);
      return this;
   }

   public MailMessage deliveryReceipt(String address)
   {
      emailMessage.addDeliveryReceiptAddress(MailUtility.internetAddress(address));
      return this;
   }

   public MailMessage readReceipt(String address)
   {
      emailMessage.addReadReceiptAddress(MailUtility.internetAddress(address));
      return this;
   }

   public MailMessage importance(MessagePriority messagePriority)
   {
      emailMessage.setImportance(messagePriority);
      return this;
   }

   public MailMessage messageId(String messageId)
   {
      emailMessage.setMessageId(messageId);
      return this;
   }

   public MailMessage textBody(String text)
   {
      emailMessage.setTextBody(text);
      return this;
   }

   public MailMessage htmlBody(String html)
   {
      emailMessage.setHtmlBody(html);
      return this;
   }

   public MailMessage htmlBodyTextAlt(String html, String text)
   {
      emailMessage.setTextBody(text);
      emailMessage.setHtmlBody(html);
      return this;
   }

   // Begin Attachments   

   public MailMessage addAttachment(EmailAttachment attachment)
   {
      emailMessage.addAttachment(attachment);
      return this;
   }

   public MailMessage addAttachment(Collection<EmailAttachment> attachments)
   {
      emailMessage.addAttachments(attachments);
      return this;
   }

   // End Attachments

   // Begin Calendar

   public MailMessage iCal(String html, byte[] bytes)
   {
      emailMessage.setType(EmailMessageType.INVITE_ICAL);
      emailMessage.setHtmlBody(html);
      emailMessage.addAttachment(new BaseEmailAttachment(null, "text/calendar;method=CANCEL", ContentDisposition.INLINE, bytes, "urn:content-classes:calendarmessage"));
      return this;
   }

   // End Calendar

   public EmailMessage getEmailMessage()
   {
      return emailMessage;
   }

   public void setEmailMessage(EmailMessage emailMessage)
   {
      this.emailMessage = emailMessage;
   }

   public EmailMessage send(Session session) throws SendFailedException
   {
      MailUtility.send(emailMessage, session);

      return emailMessage;
   }

   public EmailMessage send() throws SendFailedException
   {
      return this.send(session.get());
   }
}
