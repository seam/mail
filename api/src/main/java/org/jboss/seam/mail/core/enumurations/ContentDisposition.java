package org.jboss.seam.mail.core.enumurations;

import javax.mail.internet.MimeBodyPart;

/**
 * Defines the available Dispostions for attachments in an email Message.
 * 
 * <p>INLINE   is used where an attachment should be displayed in the body of the
 * message such as a image reference in an HTML message body</p>
 * 
 * <p>ATTACHMENT is used for standard file attachments to a message.</p>
 * 
 * @author Cody Lerum
 * 
 */
public enum ContentDisposition
{
   ATTACHMENT(MimeBodyPart.ATTACHMENT), INLINE(MimeBodyPart.INLINE);

   private String headerValue;

   private ContentDisposition(String headerValue)
   {
      this.headerValue = headerValue;
   }

   public String headerValue()
   {
      return headerValue;
   }

   public static ContentDisposition mapValue(String value)
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
         throw new UnsupportedOperationException("Unsupported Content DispostionType: " + value);
      }
   }
}
