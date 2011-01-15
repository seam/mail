package org.jboss.seam.mail.core;

/**
 * Thrown when an email address fails to validate as RFC822
 * 
 * @author Cody Lerum
 * 
 */
public class InvalidAddressException extends MailException
{
   private static final long serialVersionUID = 1L;

   public InvalidAddressException()
   {
      super();
   }

   public InvalidAddressException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public InvalidAddressException(String message)
   {
      super(message);
   }

   public InvalidAddressException(Throwable cause)
   {
      super(cause);
   }
}
