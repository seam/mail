package org.jboss.seam.mail.core.enumurations;

import javax.mail.internet.MimeBodyPart;

import org.jboss.seam.mail.exception.SeamMailException;

public enum ContentDisposition
{
   ATTACHMENT(MimeBodyPart.ATTACHMENT),
   INLINE(MimeBodyPart.INLINE);

   private String headerValue;

   private ContentDisposition(String headerValue)
   {
      this.headerValue = headerValue;
   }

   public String headerValue()
   {
      return headerValue;
   }

   public static ContentDisposition mapValue(String value) throws SeamMailException
   {
      if (value.equals(MimeBodyPart.ATTACHMENT))
      {
         return ContentDisposition.ATTACHMENT;
      }
      else if (value.equals(MimeBodyPart.INLINE))
      {
         return ContentDisposition.INLINE;
      }
      else
      {
         throw new SeamMailException("Unsupported Content DispostionType: " + value);
      }
   }
}
