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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.jboss.seam.mail.templating.TemplatingException;
import org.jboss.seam.mail.templating.VelocityTemplate;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class VelocityTextTemplate implements VelocityTemplate
{
   public String content;

   public VelocityTextTemplate(String content)
   {
      this.content = content;
   }

   public InputStream getInputStream()
   {
      try
      {
         return new ByteArrayInputStream(content.getBytes("UTF-8"));
      }
      catch (UnsupportedEncodingException e)
      {
         throw new TemplatingException("Unable to create template from String value", e);
      }
   }
}
