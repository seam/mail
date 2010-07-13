package org.jboss.seam.mail.templating.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.mail.Session;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jboss.seam.mail.annotations.Module;
import org.jboss.seam.mail.annotations.Velocity;
import org.jboss.seam.mail.api.VelocityMailMessage;
import org.jboss.seam.mail.core.BaseMailMessage;
import org.jboss.seam.mail.core.MailContext;
import org.jboss.seam.mail.exception.SeamMailException;
import org.jboss.seam.mail.templating.MailTemplate;
import org.jboss.seam.mail.templating.exception.SeamTemplatingException;
import org.jboss.weld.extensions.resourceLoader.ResourceProvider;

@Velocity
public class VelocityMailMessageImpl extends BaseMailMessage<VelocityMailMessage> implements VelocityMailMessage
{

   private VelocityEngine velocityEngine;
   private SeamBaseVelocityContext context;

   private MailTemplate textTemplate;
   private MailTemplate htmlTemplate;

   @Inject
   private ResourceProvider resourceProvider;

   @Inject
   public VelocityMailMessageImpl(@Module Session session, @Module SeamCDIVelocityContext seamCDIVelocityContext) throws SeamMailException
   {
      super(session);
      velocityEngine = new VelocityEngine();
      velocityEngine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
      context = new SeamBaseVelocityContext(seamCDIVelocityContext);
      put("mailContext", new MailContext(super.getAttachments()));
   }

   public VelocityMailMessageImpl setTemplateHTMLTextAlt(String htmlTemplatePath, String textTemplatePath) throws SeamMailException
   {
      setTemplateHTML(htmlTemplatePath);
      setTemplateText(textTemplatePath);
      return this;
   }

   public VelocityMailMessageImpl setTemplateHTML(String htmlTemplatePath) throws SeamMailException
   {
      try
      {
         htmlTemplate = createTemplate(htmlTemplatePath);
      }
      catch (SeamTemplatingException e)
      {
         throw new SeamMailException("Unable to add HTML template to MimeMessage", e);
      }
      return this;
   }

   public VelocityMailMessageImpl setTemplateText(String textTemplatePath) throws SeamMailException
   {
      try
      {
         textTemplate = createTemplate(textTemplatePath);
      }
      catch (SeamTemplatingException e)
      {
         throw new SeamMailException("Unable to add Text template to MimeMessage", e);
      }
      return this;
   }

   private MailTemplate createTemplate(String templatePath) throws SeamTemplatingException
   {
      InputStream inputStream;
      try
      {
         inputStream = resourceProvider.loadResourceStream(templatePath);
      }
      catch (Exception e)
      {
         throw new SeamTemplatingException("Unable to find template " + templatePath, e);
      }

      MailTemplate template = new MailTemplate(templatePath, inputStream);

      return template;
   }

   private String mergeTemplate(MailTemplate template) throws SeamTemplatingException
   {
      StringWriter writer = new StringWriter();
      try
      {
         velocityEngine.evaluate(context, writer, template.getName(), new InputStreamReader(template.getInputStream()));
      }
      catch (ResourceNotFoundException e)
      {
         throw new SeamTemplatingException("Unable to find template", e);
      }
      catch (ParseErrorException e)
      {
         throw new SeamTemplatingException("Unable to find template", e);
      }
      catch (MethodInvocationException e)
      {
         throw new SeamTemplatingException("Error processing method referenced in context", e);
      }
      catch (IOException e)
      {
         throw new SeamTemplatingException("Error rendering output", e);
      }
      return writer.toString();
   }

   public VelocityMailMessageImpl put(String key, Object value)
   {
      context.put(key, value);
      return this;
   }

   @Override
   public void send() throws SeamMailException
   {
      if (htmlTemplate != null && textTemplate != null)
      {
         try
         {
            super.setHTMLTextAlt(mergeTemplate(htmlTemplate), mergeTemplate(textTemplate));
         }
         catch (SeamTemplatingException e)
         {
            throw new SeamMailException("Error sending message", e);
         }
      }
      else if (htmlTemplate != null)
      {
         try
         {
            super.setHTML(mergeTemplate(htmlTemplate));
         }
         catch (SeamTemplatingException e)
         {
            throw new SeamMailException("Error sending message", e);
         }
      }
      else if (textTemplate != null)
      {
         try
         {
            super.setText(mergeTemplate(textTemplate));
         }
         catch (SeamTemplatingException e)
         {
            throw new SeamMailException("Error sending message", e);
         }
      }
      else
      {
         throw new SeamMailException("No Body was set");
      }
      super.send();
   }

   /**
    * {@inheritDoc}
    * 
    * @see org.jboss.seam.mail.core.BaseMailMessage#getRealClass()
    */
   @Override
   protected Class<VelocityMailMessage> getRealClass()
   {
      return VelocityMailMessage.class;
   }
}
