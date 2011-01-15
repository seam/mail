package org.jboss.seam.mail.core;

public class InvalidAddressException extends RuntimeException
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
