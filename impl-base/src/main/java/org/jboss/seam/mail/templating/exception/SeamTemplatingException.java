package org.jboss.seam.mail.templating.exception;

public class SeamTemplatingException extends Exception
{
   private static final long serialVersionUID = -610838646516706170L;

   public SeamTemplatingException()
   {
   }

   public SeamTemplatingException(final String message)
   {
      super(message);
   }

   public SeamTemplatingException(final String message, final Exception e)
   {
      super(message, e);
   }
}