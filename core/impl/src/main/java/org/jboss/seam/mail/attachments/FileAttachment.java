package org.jboss.seam.mail.attachments;

import java.io.File;
import java.io.IOException;

import javax.activation.FileDataSource;

import org.jboss.seam.mail.core.AttachmentException;
import org.jboss.seam.mail.core.Header;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;

import com.google.common.io.Files;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class FileAttachment extends BaseAttachment
{
   public FileAttachment(File file, ContentDisposition contentDisposition)
   {
      super();

      FileDataSource fds = new FileDataSource(file);

      try
      {
         super.setFileName(fds.getName());
         super.setMimeType(fds.getContentType());
         super.setContentDisposition(contentDisposition);
         super.setBytes(Files.toByteArray(file));
      }
      catch (IOException e)
      {
         throw new AttachmentException("Wasn't able to create email attachment from File: " + file.getName(), e);
      }
   }

   public FileAttachment(File file, ContentDisposition contentDisposition, String contentClass)
   {
      this(file, contentDisposition);
      super.addHeader(new Header("Content-Class", contentClass));
   }
}
