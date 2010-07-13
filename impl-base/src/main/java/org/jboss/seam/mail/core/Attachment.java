package org.jboss.seam.mail.core;

import java.io.File;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.exception.SeamMailException;

public class Attachment extends MimeBodyPart
{

   private String id;

   public Attachment(DataSource dataSource, String fileName, ContentDisposition contentDisposition) throws SeamMailException
   {
      super();

      id = UUID.randomUUID().toString();

      try
      {
         setContentID("<" + id + ">");
      }
      catch (MessagingException e1)
      {
         throw new SeamMailException("Unable to set unique content-id on attachment");
      }

      setData(dataSource);

      try
      {
         setFileName(fileName);
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to get FileName on attachment");
      }

      setContentDisposition(contentDisposition);

   }

   public Attachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition) throws SeamMailException
   {
      this(new ByteArrayDataSource(bytes, mimeType), fileName, contentDisposition);
   }

   public Attachment(File file, String fileName, ContentDisposition contentDisposition) throws SeamMailException
   {
      this(new FileDataSource(file), fileName, contentDisposition);
   }

   public String getId()
   {
      return id;
   }

   public String getAttachmentFileName() throws SeamMailException
   {
      try
      {
         return getFileName();
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to get File Name from attachment");
      }
   }

   public ContentDisposition getContentDisposition() throws SeamMailException
   {
      try
      {
         return ContentDisposition.mapValue(getDisposition());
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to get Content-Dispostion on attachment");
      }
   }

   public void setContentDisposition(ContentDisposition contentDisposition) throws SeamMailException
   {
      try
      {
         setDisposition(contentDisposition.headerValue());
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to set Content-Dispostion on attachment");
      }
   }

   private void setData(DataSource datasource) throws SeamMailException
   {
      try
      {
         setDataHandler(new DataHandler(datasource));
      }
      catch (MessagingException e)
      {
         throw new SeamMailException("Unable to set Data on attachment");
      }
   }
}
