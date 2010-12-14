package org.jboss.seam.mail.core.enumurations;

/**
 * Defines the available message receipt headers
 * 
 * @author Cody Lerum
 */
public enum MailHeader
{
   DELIVERY_RECIEPT("Return-Receipt-To"), 
   READ_RECIEPT("Disposition-Notification-To");

   private String headerValue;

   private MailHeader(String headerValue)
   {
      this.headerValue = headerValue;
   }

   public String headerValue()
   {
      return headerValue;
   }
}
