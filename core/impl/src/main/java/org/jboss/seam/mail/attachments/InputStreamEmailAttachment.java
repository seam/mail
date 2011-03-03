package org.jboss.seam.mail.attachments;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.seam.mail.core.AttachmentException;
import org.jboss.seam.mail.core.Header;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;

import com.google.common.io.ByteStreams;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class InputStreamEmailAttachment extends BaseEmailAttachment
{
   public InputStreamEmailAttachment(InputStream inputStream, String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      super();

      try
      {
         super.setFileName(fileName);
         super.setMimeType(mimeType);
         super.setContentDisposition(contentDisposition);
         super.setBytes(ByteStreams.toByteArray(inputStream));
      }
      catch (IOException e)
      {
         throw new AttachmentException("Wasn't able to create email attachment from InputStream");
      }
   }

   public InputStreamEmailAttachment(InputStream inputStream, String fileName, String mimeType, ContentDisposition contentDisposition, String contentClass)
   {
      this(inputStream, fileName, mimeType, contentDisposition);
      super.addHeader(new Header("Content-Class", contentClass));
   }
}
