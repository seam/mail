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

package org.jboss.seam.mail.templating.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.mail.templating.FileTemplate;
import org.jboss.seam.mail.templating.InputStreamTemplate;
import org.jboss.seam.mail.templating.MailTemplate;
import org.jboss.seam.mail.templating.TemplateImpl;
import org.jboss.seam.mail.templating.TemplatingException;
import org.jboss.seam.mail.templating.StringTemplate;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class FreeMarkerTemplate implements TemplateImpl
{
   private Configuration configuration;
   private Map<String, Object> rootMap = new HashMap<String, Object>();
   private MailTemplate mailTemplate;

   public FreeMarkerTemplate(MailTemplate mailTemplate)
   {
      this.mailTemplate = mailTemplate;
      configuration = new Configuration();
      configuration.setObjectWrapper(new DefaultObjectWrapper());
   }

   public FreeMarkerTemplate(String string)
   {
      this(new StringTemplate(string));
   }

   public FreeMarkerTemplate(InputStream inputStream)
   {
      this(new InputStreamTemplate(inputStream));
   }

   public FreeMarkerTemplate(File file)
   {
      this(new FileTemplate(file));
   }

   public String merge(Map<String, Object> context)
   {
      rootMap.putAll(context);

      StringWriter writer = new StringWriter();

      try
      {
         Template template = new Template(mailTemplate.getTemplateName(), new InputStreamReader(mailTemplate.getInputStream()), configuration);
         template.process(rootMap, writer);
      }
      catch (IOException e)
      {
         throw new TemplatingException("Error creating template", e);
      }
      catch (TemplateException e)
      {
         throw new TemplatingException("Error rendering output", e);
      }

      return writer.toString();
   }
}
