package org.jboss.seam.mail.core;

public class EmailAttachment
{
   private String fileName;
   private String mimeType;
   private byte[] bytes;

   public EmailAttachment(String fileName, String mimeType, byte[] bytes)
   {
      this.fileName = fileName;
      this.mimeType = mimeType;
      this.bytes = bytes;
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

   public byte[] getBytes()
   {
      return bytes;
   }

   public void setBytes(byte[] bytes)
   {
      this.bytes = bytes;
   }
}
