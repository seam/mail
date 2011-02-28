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

import java.io.InputStream;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.mail.templating.VelocityTemplate;
import org.jboss.seam.solder.beanManager.BeanManagerAware;
import org.jboss.seam.solder.resourceLoader.ResourceProvider;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class VelocityClassPathTemplate extends BeanManagerAware implements VelocityTemplate
{
   public String fileName;

   public VelocityClassPathTemplate(String fileName)
   {
      this.fileName = fileName;
   }

   public InputStream getInputStream()
   {
      BeanManager bm = getBeanManager();
      Bean<?> bean = bm.resolve(bm.getBeans(ResourceProvider.class));
      ResourceProvider resourceProvider = (ResourceProvider) bm.getReference(bean, bean.getBeanClass(), bm.createCreationalContext(bean));
      return resourceProvider.loadResourceStream(fileName);
   }

   public String getFileName()
   {
      return fileName;
   }

   public void setFileName(String fileName)
   {
      this.fileName = fileName;
   }
}
