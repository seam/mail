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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.core.BaseEmailAttachment;
import org.jboss.seam.mail.core.EmailAttachment;
import org.jboss.seam.mail.core.EmailContact;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.MailUtility;
import org.jboss.seam.mail.core.SendFailedException;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.EmailMessageType;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.templating.MailTemplate;
import org.jboss.seam.mail.templating.TemplatingException;
import org.jboss.seam.mail.templating.VelocityMailMessage;
import org.jboss.seam.mail.templating.VelocityTemplate;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class FreeMarkerMailMessageImpl implements VelocityMailMessage
{
   private EmailMessage emailMessage;

   private MailTemplate subjectTemplate;
   private MailTemplate textTemplate;
   private MailTemplate htmlTemplate;

   private Configuration configuration;
   private Map<String, Object> rootMap = new HashMap<String, Object>();
   
   private boolean templatesMerged = false;

   @Inject
   private Instance<Session> session;

   
   public FreeMarkerMailMessageImpl()
   {
      emailMessage = new EmailMessage();
      configuration = new Configuration();
      configuration.setObjectWrapper(new DefaultObjectWrapper());
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
      emailMessage.setType(EmailMessageType.INVITE_ICAL);
      emailMessage.setHtmlBody(html);
      emailMessage.addAttachment(new BaseEmailAttachment(null, "text/calendar;method=CANCEL", ContentDisposition.INLINE, bytes, "urn:content-classes:calendarmessage"));
      return this;
   }

   // End Calendar

   public VelocityMailMessage subject(VelocityTemplate subject)
   {
      subjectTemplate = createTemplate(subject);
      return this;
   }

   public FreeMarkerMailMessageImpl bodyText(VelocityTemplate textBody)
   {
      textTemplate = createTemplate(textBody);
      return this;
   }

   public FreeMarkerMailMessageImpl bodyHtml(VelocityTemplate htmlBody)
   {
      htmlTemplate = createTemplate(htmlBody);
      return this;
   }

   public FreeMarkerMailMessageImpl bodyHtmlTextAlt(VelocityTemplate htmlBody, VelocityTemplate textBody)
   {
      bodyHtml(htmlBody);
      bodyText(textBody);
      return this;
   }  
   
   private MailTemplate createTemplate(VelocityTemplate velocityTemplate)
   {
      return new MailTemplate("rawInput", velocityTemplate.getInputStream());
   }
   
   private String mergeTemplate(MailTemplate mailTemplate)
   {
      StringWriter writer = new StringWriter();      

      try
      {
         Template template = new Template("rawTemplate", new InputStreamReader(mailTemplate.getInputStream()), configuration);
         template.process(rootMap, writer);
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (TemplateException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return writer.toString();
   }

   public FreeMarkerMailMessageImpl put(String key, Object value)
   {
      rootMap.put(key, value);
      return this;
   }

   public EmailMessage getEmailMessage()
   {
      return emailMessage;
   }

   private FreeMarkerMailMessageImpl mergeTemplates()
   {
      if (!templatesMerged)
      {
         for (EmailAttachment ea : emailMessage.getAttachments())
         {
            if (ea.getContentDisposition() == ContentDisposition.INLINE)
            {
               if (ea.getFileName() != null && ea.getFileName().length() > 0)
               {
                  put("attach:" + ea.getFileName(), ea.getContentId());
               }
            }
         }

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
