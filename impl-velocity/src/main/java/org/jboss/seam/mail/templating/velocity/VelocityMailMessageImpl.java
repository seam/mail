package org.jboss.seam.mail.templating.velocity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Collection;

import javax.inject.Inject;
import javax.mail.Session;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jboss.seam.mail.core.EmailAttachment;
import org.jboss.seam.mail.core.EmailContact;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.MailContext;
import org.jboss.seam.mail.core.MailUtility;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.templating.MailTemplate;
import org.jboss.seam.mail.templating.VelocityMailMessage;
import org.jboss.seam.solder.resourceLoader.ResourceProvider;

public class VelocityMailMessageImpl implements VelocityMailMessage
{
   private EmailMessage emailMessage;
   private VelocityEngine velocityEngine;
   private SeamBaseVelocityContext context;

   private MailTemplate textTemplate;
   private MailTemplate htmlTemplate;
   
   @Inject
   private ResourceProvider resourceProvider;

   @Inject
   public VelocityMailMessageImpl(SeamCDIVelocityContext seamCDIVelocityContext)
   {
      emailMessage = new EmailMessage();
      velocityEngine = new VelocityEngine();
      velocityEngine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
      context = new SeamBaseVelocityContext(this, seamCDIVelocityContext);
      put("mailContext", new MailContext(MailUtility.getEmailAttachmentMap(emailMessage.getAttachments())));
   }

   // Begin Addressing

   public VelocityMailMessage from(String address)
   {
      emailMessage.setFromAddress(new EmailContact(address));
      return this;
   }

   public VelocityMailMessage from(String name, String address)
   {
      emailMessage.setFromAddress(new EmailContact(name, address));
      return this;
   }

   public VelocityMailMessage from(EmailContact emailContact)
   {
      emailMessage.setFromAddress(emailContact);
      return this;
   }

   public VelocityMailMessage replyTo(String address)
   {
      emailMessage.addReplyToAddress(new EmailContact(address));
      return this;
   }

   public VelocityMailMessage replyTo(String name, String address)
   {
      emailMessage.addReplyToAddress(new EmailContact(name, address));
      return this;
   }

   public VelocityMailMessage replyTo(EmailContact emailContact)
   {
      emailMessage.addReplyToAddress(emailContact);
      return this;
   }

   public VelocityMailMessage replyTo(Collection<EmailContact> emailContacts)
   {
      emailMessage.addReplyToAddresses(emailContacts);
      return this;
   }

   public VelocityMailMessage to(String address)
   {
      emailMessage.addToAddress(new EmailContact(address));
      return this;
   }

   public VelocityMailMessage to(String name, String address)
   {
      emailMessage.addToAddress(new EmailContact(name, address));
      return this;
   }

   public VelocityMailMessage to(EmailContact emailContact)
   {
      emailMessage.addToAddress(emailContact);
      return this;
   }

   public VelocityMailMessage to(Collection<EmailContact> emailContacts)
   {
      emailMessage.addToAddresses(emailContacts);
      return this;
   }

   public VelocityMailMessage cc(String address)
   {
      emailMessage.addCcAddress(new EmailContact(address));
      return this;
   }

   public VelocityMailMessage cc(String name, String address)
   {
      emailMessage.addCcAddress(new EmailContact(name, address));
      return this;
   }

   public VelocityMailMessage cc(EmailContact emailContact)
   {
      emailMessage.addCcAddress(emailContact);
      return this;
   }

   public VelocityMailMessage cc(Collection<EmailContact> emailContacts)
   {
      emailMessage.addCcAddresses(emailContacts);
      return this;
   }

   public VelocityMailMessage bcc(String address)
   {
      emailMessage.addBccAddress(new EmailContact(address));
      return this;
   }

   public VelocityMailMessage bcc(String name, String address)
   {
      emailMessage.addBccAddress(new EmailContact(name, address));
      return this;
   }

   public VelocityMailMessage bcc(EmailContact emailContact)
   {
      emailMessage.addBccAddress(emailContact);
      return this;
   }

   public VelocityMailMessage bcc(Collection<EmailContact> emailContacts)
   {
      emailMessage.addBccAddresses(emailContacts);
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
      emailMessage.addDeliveryReceiptAddress(address);
      return this;
   }

   public VelocityMailMessage readReceipt(String address)
   {
      emailMessage.addReadReceiptAddress(address);
      return this;
   }

   public VelocityMailMessage importance(MessagePriority messagePriority)
   {
      emailMessage.setImportance(messagePriority);
      return this;
   }

   public VelocityMailMessage textBody(String text)
   {
      emailMessage.setTextBody(text);
      return this;
   }

   public VelocityMailMessage htmlBody(String html)
   {
      emailMessage.setHtmlBody(html);
      return this;
   }

   public VelocityMailMessage htmlBodyTextAlt(String html, String text)
   {
      emailMessage.setTextBody(text);
      emailMessage.setHtmlBody(html);
      return this;
   }

// Begin Attachments

   public VelocityMailMessage addAttachment(File file, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(MailUtility.getEmailAttachment(file, contentDisposition));
      return this;
   }  

   public VelocityMailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      InputStream inputStream = resourceProvider.loadResourceStream(fileName);
      emailMessage.addAttachment(MailUtility.getEmailAttachment(fileName, inputStream, mimeType, contentDisposition));
      return this;
   }

   public VelocityMailMessage addAttachment(URL url, String fileName, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(MailUtility.getEmailAttachment(url, fileName, contentDisposition));
      return this;
   }

   public VelocityMailMessage addAttachment(byte[] bytes, String fileName, String mimeType, String contentClass, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(MailUtility.getEmailAttachment(bytes, fileName, mimeType, contentClass, contentDisposition));
      return this;
   }

   public VelocityMailMessage addAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      emailMessage.addAttachment(MailUtility.getEmailAttachment(bytes, fileName, mimeType, contentDisposition));
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
      emailMessage.addAttachment(MailUtility.getEmailAttachment(bytes, null, "text/calendar;method=CANCEL", "urn:content-classes:calendarmessage", ContentDisposition.INLINE));
      return this;
   }

   // End Calendar

   public VelocityMailMessage setTemplateText(File textTemplateFile)
   {
      textTemplate = createTemplate(textTemplateFile);
      return this;
   }

   public VelocityMailMessage setTemplateHTML(File htmlTemplateFile)
   {
      htmlTemplate = createTemplate(htmlTemplateFile);
      return this;
   }

   public VelocityMailMessage setTemplateHTMLTextAlt(File htmlTemplateFile, File textTemplateFile)
   {
      setTemplateHTML(htmlTemplateFile);
      setTemplateText(textTemplateFile);
      return this;
   }

   public VelocityMailMessageImpl setTemplateText(String text)
   {
      textTemplate = createTemplate(text);
      return this;
   }

   public VelocityMailMessageImpl setTemplateHTML(String html)
   {
      this.htmlTemplate = createTemplate(html);
      return this;
   }

   public VelocityMailMessageImpl setTemplateHTMLTextAlt(String html, String text)
   {
      setTemplateHTML(html);
      setTemplateText(text);
      return this;
   }

   private MailTemplate createTemplate(String value)
   {
      InputStream inputStream;

      try
      {
         inputStream = new ByteArrayInputStream(value.getBytes("UTF-8"));
      }
      catch (UnsupportedEncodingException e)
      {
         throw new RuntimeException("Unable to create template from rawText", e);
      }

      MailTemplate template = new MailTemplate("rawInput", inputStream);

      return template;
   }

   private MailTemplate createTemplate(File templateFile)
   {
      InputStream inputStream;

      try
      {
         inputStream = new FileInputStream(templateFile);
      }
      catch (FileNotFoundException e)
      {
         throw new RuntimeException("Unable to find template " + templateFile.getName(), e);
      }

      MailTemplate template = new MailTemplate(templateFile.getName(), inputStream);

      return template;
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
         throw new RuntimeException("Unable to find template", e);
      }
      catch (ParseErrorException e)
      {
         throw new RuntimeException("Unable to find template", e);
      }
      catch (MethodInvocationException e)
      {
         throw new RuntimeException("Error processing method referenced in context", e);
      }
      catch (IOException e)
      {
         throw new RuntimeException("Error rendering output", e);
      }
      return writer.toString();
   }

   public VelocityMailMessageImpl put(String key, Object value)
   {
      context.put(key, value);
      return this;
   }

   public EmailMessage send(Session session)
   {
      if (htmlTemplate != null)
      {
         emailMessage.setHtmlBody(mergeTemplate(htmlTemplate));
      }

      if (textTemplate != null)
      {
         emailMessage.setTextBody(mergeTemplate(textTemplate));
      }

      MailUtility.send(emailMessage, session);

      return emailMessage;
   }  
}
