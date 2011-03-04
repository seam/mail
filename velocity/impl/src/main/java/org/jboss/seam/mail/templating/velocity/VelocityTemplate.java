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
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jboss.seam.mail.templating.MailTemplate;
import org.jboss.seam.mail.templating.TemplateImpl;
import org.jboss.seam.mail.templating.TemplatingException;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class VelocityTemplate implements TemplateImpl
{
   private VelocityEngine velocityEngine;
   private VelocityContext velocityContext;
   private CDIVelocityContext cdiContext;

   private MailTemplate mailTemplate;

   public VelocityTemplate(MailTemplate mailTemplate)
   {
      velocityEngine = new VelocityEngine();
      velocityEngine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
      this.mailTemplate = mailTemplate;
   }
   
   public VelocityTemplate(MailTemplate mailTemplate, CDIVelocityContext cdiContext)
   {
      this(mailTemplate);
      this.cdiContext = cdiContext;
   }  

   public String merge(Map<String, Object> context)
   {
      StringWriter writer = new StringWriter();
      
      if(cdiContext != null)
      {
         velocityContext = new VelocityContext(context, cdiContext);
      }

      try
      {
         velocityEngine.evaluate(velocityContext, writer, mailTemplate.getTemplateName(), new InputStreamReader(mailTemplate.getInputStream()));
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
}
