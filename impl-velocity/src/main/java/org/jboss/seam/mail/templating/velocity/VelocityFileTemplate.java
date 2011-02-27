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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jboss.seam.mail.templating.TemplatingException;
import org.jboss.seam.mail.templating.VelocityTemplate;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class VelocityFileTemplate implements VelocityTemplate
{
   public File file;

   public VelocityFileTemplate(File file)
   {
      this.file = file;
   }

   public InputStream getInputStream()
   {
      try
      {
         return new FileInputStream(file);
      }
      catch (FileNotFoundException e)
      {
         throw new TemplatingException("Unable to find template file " + file.getName(), e);
      }
   }
}
