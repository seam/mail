package org.jboss.seam.mail.core;

/**
 * Any exception that is raised by the mail module extends from this runtime
 * exception class, making it easy for other modules and extensions to catch all
 * mail-related exceptions in a single catch block, if need be.
 * 
 * @author Cody Lerum
 */
public abstract class MailException extends RuntimeException
{
   private static final long serialVersionUID = 1L;

   public MailException()
   {
      super();
   }

   public MailException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public MailException(String message)
   {
      super(message);
   }

   public MailException(Throwable cause)
   {
      super(cause);
   }
}