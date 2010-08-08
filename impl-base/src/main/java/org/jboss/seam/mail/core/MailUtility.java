package org.jboss.seam.mail.core;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.mail.internet.InternetAddress;

public class MailUtility
{
   public static InternetAddress getInternetAddress(EmailContact emailContact)
   {
      try
      {
         return new InternetAddress(emailContact.getEmailAddress(), emailContact.getName(), emailContact.getCharset());
      }
      catch (UnsupportedEncodingException e)
      {
         throw new RuntimeException("Unable convert recipient to InternetAddress", e);
      }
   }

   public static InternetAddress[] getInternetAddressses(EmailContact[] recipients)
   {
      InternetAddress[] internetAddresses = new InternetAddress[recipients.length];

      for (int i = 0; i < recipients.length; i++)
      {
         internetAddresses[i] = getInternetAddress(recipients[i]);
      }

      return internetAddresses;
   }

   public static InternetAddress[] getInternetAddressses(Collection<EmailContact> recipients)
   {
      return getInternetAddressses(recipients.toArray(new EmailContact[recipients.size()]));
   }
}
