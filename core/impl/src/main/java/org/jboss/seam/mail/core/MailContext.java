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

import java.util.Map;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class MailContext
{

   private Map<String, EmailAttachment> attachments;

   public MailContext(Map<String, EmailAttachment> attachments)
   {
      this.attachments = attachments;
   }

   public String insert(String fileName)
   {
      EmailAttachment attachment = null;

      attachment = attachments.get(fileName);

      if (attachment == null)
      {
         throw new RuntimeException("Unable to find attachment: " + fileName);
      }
      else
      {
         return "cid:" + attachment.getContentId();
      }
   }
}
