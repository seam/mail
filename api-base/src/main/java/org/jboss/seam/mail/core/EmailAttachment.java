package org.jboss.seam.mail.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class EmailAttachment
{
   private String uid;
   private String fileName;
   private String mimeType;
   private ContentDisposition contentDisposition;
   private Collection<Header> headers = new ArrayList<Header>();
   private byte[] bytes;

   public EmailAttachment(String fileName, String mimeType, ContentDisposition contentDisposition, byte[] bytes)
   {
      this.uid = UUID.randomUUID().toString();
      this.fileName = fileName;
      this.mimeType = mimeType;
      this.contentDisposition = contentDisposition;
      this.bytes = bytes;
   }

   public String getUid()
   {
      return uid;
   }

   public String getFileName()
   {
      return fileName;
   }

   public void setFileName(String fileName)
   {
      this.fileName = fileName;
   }

   public String getMimeType()
   {
      return mimeType;
   }

   public void setMimeType(String mimeType)
   {
      this.mimeType = mimeType;
   }

   public ContentDisposition getContentDisposition()
   {
      return contentDisposition;
   }

   public void setContentDisposition(ContentDisposition contentDisposition)
   {
      this.contentDisposition = contentDisposition;
   }

   public Collection<Header> getHeaders()
   {
      return headers;
   }

   public void addHeader(Header header)
   {
      headers.add(header);
   }

   public void addHeaders(Collection<Header> headers)
   {
      headers.addAll(headers);
   }

   public byte[] getBytes()
   {
      return bytes;
   }

   public void setBytes(byte[] bytes)
   {
      this.bytes = bytes;
   }
}
