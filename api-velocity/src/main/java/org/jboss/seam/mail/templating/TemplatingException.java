package org.jboss.seam.mail.templating;

import org.jboss.seam.mail.core.MailException;

/**
 * Thrown when an email address fails to validate as RFC822
 * 
 * @author Cody Lerum
 * 
 */
public class TemplatingException extends MailException
{
   private static final long serialVersionUID = 1L;

   public TemplatingException()
   {
      super();
   }

   public TemplatingException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public TemplatingException(String message)
   {
      super(message);
   }

   public TemplatingException(Throwable cause)
   {
      super(cause);
   }
}
