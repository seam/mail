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

package org.jboss.seam.mail.templating.velocity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jboss.seam.mail.core.EmailAttachment;
import org.jboss.seam.mail.core.EmailContact;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.MailContext;
import org.jboss.seam.mail.core.MailUtility;
import org.jboss.seam.mail.core.SendFailedException;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.templating.MailTemplate;
import org.jboss.seam.mail.templating.TemplatingException;
import org.jboss.seam.mail.templating.VelocityMailMessage;
import org.jboss.seam.mail.templating.VelocityTemplate;
import org.jboss.seam.mail.util.EmailAttachmentUtil;
import org.jboss.seam.solder.resourceLoader.ResourceProvider;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class VelocityMailMessageImpl implements VelocityMailMessage
{
   private EmailMessage emailMessage;
   private VelocityEngine velocityEngine;
   private SeamBaseVelocityContext context;

   private MailTemplate subjectTemplate;
   private MailTemplate textTemplate;
   private MailTemplate htmlTemplate;

   private boolean templatesMerged = false;

   @Inject
   private ResourceProvider resourceProvider;

   @Inject
   private Instance<Session> session;

   @Inject
   public VelocityMailMessageImpl(SeamCDIVelocityContext seamCDIVelocityContext)
   {
      emailMessage = new EmailMessage();
      velocityEngine = new VelocityEngine();
      velocityEngine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
      context = new SeamBaseVelocityContext(this, seamCDIVelocityContext);
   }

   // Begin Addressing

   public VelocityMailMessage from(String address)
   {
      emailMessage.addFromAddress(MailUtility.internetAddress(address));
      return this;
   }

   public VelocityMailMessage from(String address, String name)
   {
      emailMessage.addFromAddress(MailUtility.internetAddress(address, name));
      return this;
   }

   public VelocityMailMessage from(InternetAddress emailAddress)
   {
      emailMessage.addFromAddress(emailAddress);
      return this;
   }

   public VelocityMailMessage from(EmailContact emailContact)
   {
      emailMessage.addFromAddress(MailUtility.internetAddress(emailContact));
      return this;
   }

   public VelocityMailMessage from(Collection<EmailContact> emailContacts)
   {
      emailMessage.addFromAddresses(MailUtility.internetAddress(emailContacts));
      return this;
   }

   public VelocityMailMessage replyTo(String address)
   {
      emailMessage.addReplyToAddress(MailUtility.internetAddress(address));
      return this;
   }

   public VelocityMailMessage replyTo(String address, String name)
   {
      emailMessage.addReplyToAddress(MailUtility.internetAddress(address, name));
      return this;
   }

   public VelocityMailMessage replyTo(InternetAddress emailAddress)
   {
      emailMessage.addReplyToAddress(emailAddress);
      return this;
   }

   public VelocityMailMessage replyTo(EmailContact emailContact)
   {
      emailMessage.addReplyToAddress(MailUtility.internetAddress(emailContact));
      return this;
   }

   public VelocityMailMessage replyTo(Collection<EmailContact> emailContacts)
   {
      emailMessage.addReplyToAddresses(MailUtility.internetAddress(emailContacts));
      return this;
   }

   public VelocityMailMessage to(String address)
   {
      emailMessage.addToAddress(MailUtility.internetAddress(address));
      return this;
   }

   public VelocityMailMessage to(String address, String name)
   {
      emailMessage.addToAddress(MailUtility.internetAddress(address, name));
      return this;
   }

   public VelocityMailMessage to(InternetAddress emailAddress)
   {
      emailMessage.addToAddress(emailAddress);
      return this;
   }

   public VelocityMailMessage to(EmailContact emailContact)
   {
      emailMessage.addToAddress(MailUtility.internetAddress(emailContact));
      return this;
   }

   public VelocityMailMessage to(Collection<EmailContact> emailContacts)
   {
      emailMessage.addToAddresses(MailUtility.internetAddress(emailContacts));
      return this;
   }

   public VelocityMailMessage cc(String address)
   {
      emailMessage.addCcAddress(MailUtility.internetAddress(address));
      return this;
   }

   public VelocityMailMessage cc(String address, String name)
   {
      emailMessage.addCcAddress(MailUtility.internetAddress(address, name));
      return this;
   }

   public VelocityMailMessage cc(InternetAddress emailAddress)
   {
      emailMessage.addCcAddress(emailAddress);
      return this;
   }

   public VelocityMailMessage cc(EmailContact emailContact)
   {
      emailMessage.addCcAddress(MailUtility.internetAddress(emailContact));
      return this;
   }

   public VelocityMailMessage cc(Collection<EmailContact> emailContacts)
   {
      emailMessage.addCcAddresses(MailUtility.internetAddress(emailContacts));
      return this;
   }

   public VelocityMailMessage bcc(String address)
   {
      emailMessage.addBccAddress(MailUtility.internetAddress(address));
      return this;
   }

   public VelocityMailMessage bcc(String address, String name)
   {
      emailMessage.addBccAddress(MailUtility.internetAddress(address, name));
      return this;
   }

   public VelocityMailMessage bcc(InternetAddress emailAddress)
   {
      emailMessage.addBccAddress(emailAddress);
      return this;
   }

   public VelocityMailMessage bcc(EmailContact emailContact)
   {
      emailMessage.addBccAddress(MailUtility.internetAddress(emailContact));
      return this;
   }

   public VelocityMailMessage bcc(Collection<EmailContact> emailContacts)
   {
      emailMessage.addBccAddresses(MailUtility.internetAddress(emailContacts));
      return this;
   }

   // End Addressing

   public VelocityMailMessage subject(String value)
   {
      emailMessage.setSubject(value);
      return this;
   }

   public VelocityMailMessage deliveryReceipt(String address)
   {
      emailMessage.addDeliveryReceiptAddress(MailUtility.internetAddress(address));
      return this;
   }

   public VelocityMailMessage readReceipt(String address)
   {
      emailMessage.addReadReceiptAddress(MailUtility.internetAddress(address));
      return this;
   }

   public VelocityMailMessage importance(MessagePriority messagePriority)
   {
      emailMessage.setImportance(messagePriority);
      return this;
   }

   public VelocityMailMessage messageId(String messageId)
   {
      emailMessage.setMessageId(messageId);
      return this;
   }

   public VelocityMailMessage bodyText(String text)
   {
      emailMessage.setTextBody(text);
      return this;
   }

   public VelocityMailMessage bodyHtml(String html)
   {
      emailMessage.setHtmlBody(html);
      return this;
   }

   public VelocityMailMessage bodyHtmlTextAlt(String html, String text)
   {
      emailMessage.setTextBody(text);
      emailMessage.setHtmlBody(html);
      return this;
   }

   // Begin Attachments

   public VelocityMailMessage addAttachment(File file, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(EmailAttachmentUtil.getEmailAttachment(file, contentDisposition));
      return this;
   }

   public VelocityMailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      InputStream inputStream = resourceProvider.loadResourceStream(fileName);
      emailMessage.addAttachment(EmailAttachmentUtil.getEmailAttachment(fileName, inputStream, mimeType, contentDisposition));
      return this;
   }

   public VelocityMailMessage addAttachment(URL url, String fileName, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(EmailAttachmentUtil.getEmailAttachment(url, fileName, contentDisposition));
      return this;
   }

   public VelocityMailMessage addAttachment(byte[] bytes, String fileName, String mimeType, String contentClass, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(EmailAttachmentUtil.getEmailAttachment(bytes, fileName, mimeType, contentClass, contentDisposition));
      return this;
   }

   public VelocityMailMessage addAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(EmailAttachmentUtil.getEmailAttachment(bytes, fileName, mimeType, contentDisposition));
      return this;
   }

   public VelocityMailMessage addAttachment(EmailAttachment attachment)
   {
      emailMessage.addAttachment(attachment);
      return this;
   }

   public VelocityMailMessage addAttachment(Collection<EmailAttachment> attachments)
   {
      emailMessage.addAttachments(attachments);
      return this;
   }

   // End Attachments

   // Begin Calendar

   public VelocityMailMessage iCal(String html, byte[] bytes)
   {
      emailMessage.setHtmlBody(html);
      emailMessage.addAttachment(EmailAttachmentUtil.getEmailAttachment(bytes, null, "text/calendar;method=CANCEL", "urn:content-classes:calendarmessage", ContentDisposition.INLINE));
      return this;
   }

   // End Calendar

   public VelocityMailMessage subject(VelocityTemplate subject)
   {
      subjectTemplate = createTemplate(subject);
      return this;
   }

   public VelocityMailMessageImpl bodyText(VelocityTemplate textBody)
   {
      textTemplate = createTemplate(textBody);
      return this;
   }

   public VelocityMailMessageImpl bodyHtml(VelocityTemplate htmlBody)
   {
      htmlTemplate = createTemplate(htmlBody);
      return this;
   }

   public VelocityMailMessageImpl bodyHtmlTextAlt(VelocityTemplate htmlBody, VelocityTemplate textBody)
   {
      bodyHtml(htmlBody);
      bodyText(textBody);
      return this;
   }  
   
   private MailTemplate createTemplate(VelocityTemplate velocityTemplate)
   {
      return new MailTemplate("rawInput", velocityTemplate.getInputStream());
   }
   
   private String mergeTemplate(MailTemplate template)
   {
      StringWriter writer = new StringWriter();

      try
      {
         velocityEngine.evaluate(context, writer, template.getName(), new InputStreamReader(template.getInputStream()));
      }
      catch (ResourceNotFoundException e)
      {
         throw new TemplatingException("Unable to find template", e);
      }
      catch (ParseErrorException e)
      {
         throw new TemplatingException("Unable to find template", e);
      }
      catch (MethodInvocationException e)
      {
         throw new TemplatingException("Error processing method referenced in context", e);
      }
      catch (IOException e)
      {
         throw new TemplatingException("Error rendering output", e);
      }

      return writer.toString();
   }

   public VelocityMailMessageImpl put(String key, Object value)
   {
      context.put(key, value);
      return this;
   }

   public EmailMessage getEmailMessage()
   {
      return emailMessage;
   }

   private VelocityMailMessageImpl mergeTemplates()
   {
      if (!templatesMerged)
      {
         put("mailContext", new MailContext(EmailAttachmentUtil.getEmailAttachmentMap(emailMessage.getAttachments())));

         if (subjectTemplate != null)
         {
            emailMessage.setSubject(mergeTemplate(subjectTemplate));
         }

         if (textTemplate != null)
         {
            emailMessage.setTextBody(mergeTemplate(textTemplate));
         }

         if (htmlTemplate != null)
         {
            emailMessage.setHtmlBody(mergeTemplate(htmlTemplate));
         }

         templatesMerged = true;

         return this;
      }
      else
      {
         throw new TemplatingException("Email Templates Already Merged");
      }
   }

   public EmailMessage send(Session session) throws SendFailedException, TemplatingException
   {
      if (!templatesMerged)
      {
         mergeTemplates();
      }

      MailUtility.send(emailMessage, session);

      return emailMessage;
   }

   public EmailMessage send() throws SendFailedException, TemplatingException
   {
      return this.send(session.get());     
   }
}
