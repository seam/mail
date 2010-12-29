package org.jboss.seam.mail.core;

import java.util.Collection;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;

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
