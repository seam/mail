package org.jboss.seam.mail.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;

public class AttachmentPart extends MimeBodyPart
{

   private String id;

   public AttachmentPart(DataSource dataSource, String fileName, String contentClass, ContentDisposition contentDisposition)
   {
      super();

      id = UUID.randomUUID().toString();

      try
      {
         setContentID("<" + id + ">");
      }
      catch (MessagingException e1)
      {
         throw new RuntimeException("Unable to set unique content-id on attachment");
      }

      setData(dataSource);
      
      if(fileName != null)
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
      
      if(contentClass != null && contentClass.trim().length() != 0)
      {
         try
         {
            addHeader("Content-Class","urn:content-classes:calendarmessage");
         }
         catch (MessagingException e)
         {
            throw new RuntimeException("Unable to add Content-Class Header");
         }
      }      

      setContentDisposition(contentDisposition);
   }
   
   public AttachmentPart(byte[] bytes, String fileName, String mimeType, String contentClass, ContentDisposition contentDisposition)
   {
      this(getByteArrayDataSource(bytes, mimeType), fileName, contentClass, contentDisposition);
   }

   public AttachmentPart(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      this(getByteArrayDataSource(bytes, mimeType), fileName, null, contentDisposition);
   }
   
   public AttachmentPart(InputStream inputStream, String fileName, String mimeType, ContentDisposition contentDisposition)
   {         
      this(getByteArrayDataSource(inputStream, mimeType), fileName, null, contentDisposition);
   }

   public AttachmentPart(File file, String fileName, ContentDisposition contentDisposition)
   {
      this(new FileDataSource(file), fileName, null, contentDisposition);
   }

   public String getId()
   {
      return id;
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
   
   private static ByteArrayDataSource getByteArrayDataSource(byte [] bytes, String mimeType)
   {
      ByteArrayDataSource bads = new ByteArrayDataSource(bytes, mimeType);
      return bads;
   }
   
   private static ByteArrayDataSource getByteArrayDataSource(InputStream inputStream, String mimeType)
   {
      ByteArrayDataSource bads;
      try
      {
         bads = new ByteArrayDataSource(inputStream, mimeType);
      }
      catch (IOException e)
      {
         throw new RuntimeException("Unable to created Attacment from InputStream");
      }
      return bads;
   }
}
