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

import java.util.ArrayList;
import java.util.Collection;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;


/**
 * 
 * @author Cody Lerum
 * 
 */
public class AttachmentPart extends MimeBodyPart
{

   private String uid;

   public AttachmentPart(DataSource dataSource, String uid, String fileName, Collection<Header> headers, ContentDisposition contentDisposition)
   {
      super();

      this.uid = uid;

      try
      {
         setContentID("<" + uid + ">");
      }
      catch (MessagingException e1)
      {
         throw new RuntimeException("Unable to set unique content-id on attachment");
      }

      setData(dataSource);

      if (fileName != null)
      {
         try
         {
            setFileName(fileName);
         }
         catch (MessagingException e)
         {
            throw new RuntimeException("Unable to get FileName on attachment");
         }
      }

      if (headers != null)
      {
         for (Header header : headers)
         {
            try
            {
               addHeader(header.getName(), header.getValue());

            }
            catch (MessagingException e)
            {
               throw new RuntimeException("Unable to add Content-Class Header");
            }
         }
      }

      setContentDisposition(contentDisposition);
   }

   public AttachmentPart(byte[] bytes, String uid, String fileName, String mimeType, Collection<Header> headers, ContentDisposition contentDisposition)
   {
      this(getByteArrayDataSource(bytes, mimeType), uid, fileName, headers, contentDisposition);
   }

   public AttachmentPart(byte[] bytes, String uid, String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      this(getByteArrayDataSource(bytes, mimeType), uid, fileName, new ArrayList<Header>(), contentDisposition);
   }

   public String getAttachmentFileName()
   {
      try
      {
         return getFileName();
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to get File Name from attachment");
      }
   }

   public ContentDisposition getContentDisposition()
   {
      try
      {
         return ContentDisposition.mapValue(getDisposition());
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to get Content-Dispostion on attachment");
      }
   }

   public String getUid()
   {
      return uid;
   }

   public void setContentDisposition(ContentDisposition contentDisposition)
   {
      try
      {
         setDisposition(contentDisposition.headerValue());
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to set Content-Dispostion on attachment");
      }
   }

   private void setData(DataSource datasource)
   {
      try
      {
         setDataHandler(new DataHandler(datasource));
      }
      catch (MessagingException e)
      {
         throw new RuntimeException("Unable to set Data on attachment");
      }
   }

   private static ByteArrayDataSource getByteArrayDataSource(byte[] bytes, String mimeType)
   {
      ByteArrayDataSource bads = new ByteArrayDataSource(bytes, mimeType);
      return bads;
   }
}
