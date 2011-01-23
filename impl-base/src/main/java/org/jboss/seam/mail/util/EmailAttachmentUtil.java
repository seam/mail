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

package org.jboss.seam.mail.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.activation.FileDataSource;
import javax.activation.URLDataSource;

import org.jboss.seam.mail.core.EmailAttachment;
import org.jboss.seam.mail.core.Header;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
/**
 * 
 * @author Cody Lerum
 *
 */
public class EmailAttachmentUtil
{
   public static Map<String, EmailAttachment> getEmailAttachmentMap(Collection<EmailAttachment> attachments)
   {
      Map<String, EmailAttachment> emailAttachmentMap = new HashMap<String, EmailAttachment>();

      for (EmailAttachment ea : attachments)
      {
         if (!Strings.isNullOrBlank(ea.getFileName()))
         {
            emailAttachmentMap.put(ea.getFileName(), ea);
         }
      }

      return emailAttachmentMap;
   }

   public static EmailAttachment getEmailAttachment(File file, ContentDisposition contentDisposition)
   {
      FileDataSource fds = new FileDataSource(file);

      try
      {
         return new EmailAttachment(fds.getName(), fds.getContentType(), contentDisposition, Files.toByteArray(file));
      }
      catch (IOException e)
      {
         throw new RuntimeException("Was not able to create email attachment from file: " + file.getName(), e);
      }
   }

   public static EmailAttachment getEmailAttachment(String fileName, InputStream inputStream, String mimeType, ContentDisposition contentDisposition)
   {
      EmailAttachment emailAttachment;
      try
      {
         emailAttachment = new EmailAttachment(fileName, mimeType, contentDisposition, ByteStreams.toByteArray(inputStream));
      }
      catch (IOException e)
      {
         throw new RuntimeException("Was not able to create email attachment from file: " + fileName, e);
      }
      return emailAttachment;
   }

   public static EmailAttachment getEmailAttachment(URL url, String fileName, ContentDisposition contentDisposition)
   {
      URLDataSource uds = new URLDataSource(url);

      byte[] bytes;

      try
      {
         bytes = new byte[uds.getInputStream().available()];
         uds.getInputStream().read(bytes);
      }
      catch (IOException e)
      {
         throw new RuntimeException("Wasn't able to create email attachment from URL: " + url.getPath(), e);
      }

      return new EmailAttachment(fileName, uds.getContentType(), contentDisposition, bytes);
   }

   public static EmailAttachment getEmailAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      return new EmailAttachment(fileName, mimeType, contentDisposition, bytes);
   }

   public static EmailAttachment getEmailAttachment(byte[] bytes, String fileName, String mimeType, String contentClass, ContentDisposition contentDisposition)
   {
      EmailAttachment emailAttachment = getEmailAttachment(bytes, fileName, mimeType, contentDisposition);
      emailAttachment.addHeader(new Header("Content-Class", contentClass));
      return emailAttachment;
   }
}
