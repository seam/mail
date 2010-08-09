package org.jboss.seam.mail.templating.velocity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import javax.inject.Inject;
import javax.mail.Session;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jboss.seam.mail.annotations.Module;
import org.jboss.seam.mail.annotations.Velocity;
import org.jboss.seam.mail.core.BaseMailMessage;
import org.jboss.seam.mail.core.MailContext;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.templating.MailTemplate;
import org.jboss.seam.mail.templating.VelocityMailMessage;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;

@Velocity
public class VelocityMailMessageImpl extends BaseMailMessage implements VelocityMailMessage
{

   private VelocityEngine velocityEngine;
   private SeamBaseVelocityContext context;

   private MailTemplate textTemplate;
   private MailTemplate htmlTemplate;

   @Inject
   private ResourceProvider resourceProvider;

   @Inject
   public VelocityMailMessageImpl(@Module Session session, @Module SeamCDIVelocityContext seamCDIVelocityContext)
   {
      super(session);
      velocityEngine = new VelocityEngine();
      velocityEngine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
      context = new SeamBaseVelocityContext(this, seamCDIVelocityContext);
      put("mailContext", new MailContext(super.getAttachments()));
   }
   
   public VelocityMailMessage from(String name, String address)
   {
      super.setFrom(name, address);
      return this;
   }

   public VelocityMailMessage to(String name, String address)
   {
      super.addTo(name, address);
      return this;
   }
   
   public VelocityMailMessage cc(String name, String address)
   {
      super.addCc(name, address);
      return this;
   }
   
   public VelocityMailMessage bcc(String name, String address)
   {
      super.addBcc(name, address);
      return this;
   }
   
   public VelocityMailMessage subject(String value)
   {
      super.setSubject(value);
      return this;
   }  

   public VelocityMailMessage deliveryReciept(String address)
   {
      super.setDeliveryReciept(address);
      return this;
   }
   
   public VelocityMailMessage readReciept(String address)
   {
      super.setReadReciept(address);
      return this;
   }   

   public VelocityMailMessage importance(MessagePriority messagePriority)
   {
      super.setImportance(messagePriority);
      return this;
   }
   
   public VelocityMailMessage textBody(String text)
   {
      super.setText(text);
      return this;      
   }
   
   public VelocityMailMessage htmlBody(String html)
   {
      super.setHTML(html);
      return this;
   }
   
   public VelocityMailMessage htmlBodyTextAlt(String html, String text)
   {
      super.setHTMLTextAlt(html, text);
      return this;
   }

   public VelocityMailMessage addAttachment(File file, ContentDisposition contentDisposition)
   {
      super.addAttachmentImpl(file, contentDisposition);
      return this;
   }

   public VelocityMailMessage addAttachment(String fileName, ContentDisposition contentDisposition)
   {
      super.addAttachmentImpl(fileName, contentDisposition);
      return this;
   }

   public VelocityMailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      super.addAttachmentImpl(fileName, mimeType, contentDisposition);
      return this;
   }

   public VelocityMailMessage addAttachment(URL url, String fileName, ContentDisposition contentDisposition)
   {
      super.addAttachmentImpl(url, fileName, contentDisposition);
      return this;
   } 

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

   public VelocityMailMessageImpl setTemplateText(String textTemplatePath)
   {
      textTemplate = createTemplate(textTemplatePath);

      return this;
   }

   public VelocityMailMessageImpl setTemplateHTML(String htmlTemplatePath)
   {
      htmlTemplate = createTemplate(htmlTemplatePath);

      return this;
   }

   public VelocityMailMessageImpl setTemplateHTMLTextAlt(String htmlTemplatePath, String textTemplatePath)
   {
      setTemplateHTML(htmlTemplatePath);
      setTemplateText(textTemplatePath);
      return this;
   }

   private MailTemplate createTemplate(String templatePath)
   {
      InputStream inputStream;
      inputStream = resourceProvider.loadResourceStream(templatePath);

      MailTemplate template = new MailTemplate(templatePath, inputStream);

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

   @Override
   public void send()
   {
      if (htmlTemplate != null && textTemplate != null)
      {
         super.setHTMLTextAlt(mergeTemplate(htmlTemplate), mergeTemplate(textTemplate));
      }
      else if (htmlTemplate != null)
      {
         super.setHTML(mergeTemplate(htmlTemplate));
      }
      else if (textTemplate != null)
      {
         super.setText(mergeTemplate(textTemplate));
      }
      else
      {
         throw new UnsupportedOperationException("No Body was set");
      }
      super.send();
   }
}
