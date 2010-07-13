package org.jboss.seam.mail.exception;

public class SeamMailException extends Exception
{
   private static final long serialVersionUID = -610838646516706170L;

   public SeamMailException()
   {
   }

   public SeamMailException(final String message)
   {
      super(message);
   }

   public SeamMailException(final String message, final Exception e)
   {
      super(message, e);
   }
}