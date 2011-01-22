package org.jboss.seam.mail.core;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.core.enumurations.RecipientType;
import org.jboss.seam.mail.util.Strings;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class MailUtility
{
   public static InternetAddress internetAddress(String address) throws InvalidAddressException
   {
      try
      {
         return new InternetAddress(address);
      }
      catch (AddressException e)
      {
         throw new InvalidAddressException(e);
      }
   }

   public static InternetAddress internetAddress(String name, String address) throws InvalidAddressException
   {
      InternetAddress internetAddress;
      try
      {
         internetAddress = new InternetAddress(address);
         internetAddress.setPersonal(name);
         return internetAddress;
      }
      catch (AddressException e)
      {
         throw new InvalidAddressException(e);
      }
      catch (UnsupportedEncodingException e)
      {
         throw new InvalidAddressException(e);
      }
   }

   public static InternetAddress[] getInternetAddressses(InternetAddress emaiAddress)
   {
      InternetAddress[] internetAddresses = { emaiAddress };

      return internetAddresses;
   }

   public static InternetAddress[] getInternetAddressses(Collection<InternetAddress> recipients)
   {
      InternetAddress[] result = new InternetAddress[recipients.size()];
      recipients.toArray(result);
      return result;
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

   public static Session buildMailSession(MailConfig mailConfig)
   {
      Session session;

      Properties props = new Properties();

      if (mailConfig.isValid())
      {
         props.put("mail.smtp.host", mailConfig.getServerHost());
         props.put("mail.smtp.port", mailConfig.getServerPort());
      }
      else
      {
         throw new RuntimeException("ServerHost and ServerPort must be set in MailConfig");
      }

      if (!Strings.isNullOrBlank(mailConfig.getDomainName()))
      {
         props.put("mail.seam.domainName", mailConfig.getDomainName());
      }

      if (mailConfig.getUsername() != null && mailConfig.getUsername().length() != 0 && mailConfig.getPassword() != null && mailConfig.getPassword().length() != 0)
      {
         MailSessionAuthenticator authenticator = new MailSessionAuthenticator(mailConfig.getUsername(), mailConfig.getPassword());

         session = Session.getInstance(props, authenticator);
      }
      else
      {
         session = Session.getInstance(props, null);
      }
      return session;
   }

   public static String headerStripper(String header)
   {
      if (!Strings.isNullOrBlank(header))
      {
         String s = header.trim();

         if (s.matches("^<.*>$"))
         {
            return header.substring(1, header.length() - 1);
         }
         else
         {
            return header;
         }
      }
      else
      {
         return header;
      }
   }

   public static void send(EmailMessage e, Session session) throws SendFailedException
   {
      BaseMailMessage b = new BaseMailMessage(session, e.getRootContentType());

      if (!Strings.isNullOrBlank(e.getMessageId()))
      {
         b.setMessageID(e.getMessageId());
      }

      b.setFrom(e.getFromAddresses());
      b.addRecipients(RecipientType.TO, e.getToAddresses());
      b.addRecipients(RecipientType.CC, e.getCcAddresses());
      b.addRecipients(RecipientType.BCC, e.getBccAddresses());
      b.setReplyTo(e.getReplyToAddresses());
      b.addDeliveryRecieptAddresses(e.getDeliveryReceiptAddresses());
      b.addReadRecieptAddresses(e.getReadReceiptAddresses());
      b.setImportance(e.getImportance());
      b.addHeaders(e.getHeaders());

      if (e.getSubject() != null)
      {
         b.setSubject(e.getSubject());
      }

      if (e.getType() == EmailMessageType.STANDARD)
      {

         if (e.getHtmlBody() != null && e.getTextBody() != null)
         {
            b.setHTMLTextAlt(e.getHtmlBody(), e.getTextBody());
         }
         else if (e.getTextBody() != null)
         {
            b.setText(e.getTextBody());
         }
         else if (e.getHtmlBody() != null)
         {
            b.setHTML(e.getHtmlBody());
         }

         b.addAttachments(e.getAttachments());
      }
      else if (e.getType() == EmailMessageType.ICAL_INVITE)
      {
         b.setHTMLNotRelated(e.getHtmlBody());
         b.addAttachments(e.getAttachments());
      }
      else
      {
         throw new RuntimeException("Unsupported Message Type: " + e.getType());
      }
      b.send();

      try
      {
         e.setMessageId(null);
         e.setLastMessageId(MailUtility.headerStripper(b.getFinalizedMessage().getMessageID()));
      }
      catch (MessagingException e1)
      {
         throw new RuntimeException("Unable to read Message-ID from sent message");
      }
   }
}
