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
