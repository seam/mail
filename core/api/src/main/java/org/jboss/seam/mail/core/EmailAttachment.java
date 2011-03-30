package org.jboss.seam.mail.core;

import java.util.Collection;

import org.jboss.seam.mail.core.enumerations.ContentDisposition;

public interface EmailAttachment
{
   public String getContentId();

   public String getFileName();

   public String getMimeType();

   public ContentDisposition getContentDisposition();

   public Collection<Header> getHeaders();

   public byte[] getBytes();
}
