package org.jboss.seam.mail.core;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Collection;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class MailUtility
{
   public static InternetAddress getInternetAddress(EmailContact emailContact)
   {
      if (emailContact.getName() == null)
      {
         try
         {
            return new InternetAddress(emailContact.getEmailAddress(), true);
         }
         catch (AddressException e)
         {
            throw new RuntimeException("Unable convert recipient to InternetAddress", e);
         }
      }
      try
      {
         return new InternetAddress(emailContact.getEmailAddress(), emailContact.getName(), emailContact.getCharset());
      }
      catch (UnsupportedEncodingException e)
      {
         throw new RuntimeException("Unable convert recipient to InternetAddress", e);
      }
   }

   public static InternetAddress[] getInternetAddressses(EmailContact emailContact)
   {
      InternetAddress[] internetAddresses = { getInternetAddress(emailContact) };

      return internetAddresses;
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

   public static String getHostName()
   {
      try
      {
         java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
         return localMachine.getHostName();
      }
      catch (UnknownHostException e)
      {
         return "localhost";
      }
   }
}
