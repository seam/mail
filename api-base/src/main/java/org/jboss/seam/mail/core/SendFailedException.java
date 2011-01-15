package org.jboss.seam.mail.core;

/**
 * Thrown when an email fails to be sent.
 * 
 * @author Cody Lerum
 * 
 */
public class SendFailedException extends MailException
{
   private static final long serialVersionUID = 1L;

   public SendFailedException()
   {
      super();
   }

   public SendFailedException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public SendFailedException(String message)
   {
      super(message);
   }

   public SendFailedException(Throwable cause)
   {
      super(cause);
   }
}
