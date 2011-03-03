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

package org.jboss.seam.mail.core;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeUtility;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class Header implements Serializable
{
   private static final long serialVersionUID = 1L;

   private String name;
   private String value;

   public Header(String name, String value)
   {
      this.name = name;

      try
      {
         this.value = MimeUtility.fold(name.length() + 2, MimeUtility.encodeText(value));
      }
      catch (UnsupportedEncodingException e)
      {
         throw new RuntimeException("Unable to create header", e);
      }
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }
}
