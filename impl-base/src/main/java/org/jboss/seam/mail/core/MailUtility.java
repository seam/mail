package org.jboss.seam.mail.core;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.exception.SeamMailException;

public class MailUtility
{
   public static InternetAddress getInternetAddress(EmailContact emailContact) throws SeamMailException
   {
      try
      {
         return new InternetAddress(emailContact.getEmailAddress(), emailContact.getName(), emailContact.getCharset());
      }
      catch (UnsupportedEncodingException e)
      {
         throw new SeamMailException("Unable convert recipient to InternetAddress", e);
      }
   }

   public static InternetAddress[] getInternetAddressses(EmailContact[] recipients) throws SeamMailException
   {
      InternetAddress[] internetAddresses = new InternetAddress[recipients.length];

      for (int i = 0; i < recipients.length; i++)
      {
         internetAddresses[i] = getInternetAddress(recipients[i]);
      }

      return internetAddresses;
   }

   public static InternetAddress[] getInternetAddressses(Collection<EmailContact> recipients) throws SeamMailException
   {
      return getInternetAddressses(recipients.toArray(new EmailContact[recipients.size()]));
   }
}
