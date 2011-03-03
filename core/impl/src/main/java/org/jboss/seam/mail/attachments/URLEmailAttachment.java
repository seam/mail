package org.jboss.seam.mail.attachments;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.activation.URLDataSource;

import org.jboss.seam.mail.core.AttachmentException;
import org.jboss.seam.mail.core.Header;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;

public class URLEmailAttachment extends BaseEmailAttachment
{
   public URLEmailAttachment(String url, String fileName, ContentDisposition contentDisposition)
   {
      super();

      byte[] bytes;
      URLDataSource uds;

      try
      {
         uds = new URLDataSource(new URL(url));
         bytes = new byte[uds.getInputStream().available()];
         uds.getInputStream().read(bytes);

         super.setFileName(fileName);
         super.setMimeType(uds.getContentType());
         super.setContentDisposition(contentDisposition);
         super.setBytes(bytes);
      }
      catch (MalformedURLException e)
      {
         throw new AttachmentException("Wasn't able to create email attachment from URL: " + url, e);
      }
      catch (IOException e)
      {
         throw new AttachmentException("Wasn't able to create email attachment from URL: " + url, e);
      }
   }

   public URLEmailAttachment(String url, String fileName, ContentDisposition contentDisposition, String contentClass)
   {
      this(url, fileName, contentDisposition);
      super.addHeader(new Header("Content-Class", contentClass));
   }
}
