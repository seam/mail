package org.jboss.seam.mail.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.RecipientType;

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

   public static EmailAttachment getEmailAttachment(File file, ContentDisposition contentDisposition)
   {
      FileDataSource fds = new FileDataSource(file);

      try
      {
         return new EmailAttachment(fds.getName(), fds.getContentType(), contentDisposition, MailUtility.getBytesFromFile(file));
      }
      catch (IOException e)
      {
         throw new RuntimeException("Was not able to create email attachment from file: " + file.getName(), e);
      }
   }

   public static EmailAttachment getEmailAttachment(String fileName, InputStream inputStream, String mimeType, ContentDisposition contentDisposition)
   {
      EmailAttachment emailAttachment;
      try
      {
         emailAttachment = new EmailAttachment(fileName, mimeType, contentDisposition, inputStreamToByteArray(inputStream));
      }
      catch (IOException e)
      {
         throw new RuntimeException("Was not able to create email attachment from file: " + fileName, e);
      }
      return emailAttachment;
   }

   public static EmailAttachment getEmailAttachment(URL url, String fileName, ContentDisposition contentDisposition)
   {
      URLDataSource uds = new URLDataSource(url);

      byte[] bytes;
      try
      {
         bytes = new byte[uds.getInputStream().available()];
         uds.getInputStream().read(bytes);
      }
      catch (IOException e)
      {
         throw new RuntimeException("Wasn't able to create email attachment from URL: " + url.getPath(), e);

      }

      return new EmailAttachment(fileName, uds.getContentType(), contentDisposition, bytes);
   }

   public static EmailAttachment getEmailAttachment(byte[] bytes, String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      return new EmailAttachment(fileName, mimeType, contentDisposition, bytes);
   }

   public static EmailAttachment getEmailAttachment(byte[] bytes, String fileName, String mimeType, String contentClass, ContentDisposition contentDisposition)
   {
      EmailAttachment emailAttachment = getEmailAttachment(bytes, fileName, mimeType, contentDisposition);
      emailAttachment.addHeader(new Header("Content-Class", contentClass));
      return emailAttachment;
   }

   public static byte[] getBytesFromFile(File file) throws IOException
   {
      InputStream inputStream = new FileInputStream(file);
      return inputStreamToByteArray(inputStream);
   }

   public static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException
   {
      byte[] bytes = new byte[inputStream.available()];
      inputStream.read(bytes);
      return bytes;
   }

   public static void send(EmailMessage e, Session session)
   {
      BaseMailMessage b = new BaseMailMessage(session);
      b.setFrom(e.getFromAddress());
      b.addRecipients(RecipientType.TO, e.getToAddresses());
      b.addRecipients(RecipientType.CC, e.getCcAddresses());
      b.addRecipients(RecipientType.BCC, e.getBccAddresses());
      b.setReplyTo(e.getReplyToAddresses());
      b.addDeliveryRecieptAddresses(e.getDeliveryReceiptAddresses());
      b.addReadRecieptAddresses(e.getReadReceiptAddresses());
      b.setImportance(e.getImportance());
      
      if(e.getSubject() != null)
      {
         b.setSubject(e.getSubject());
      }      

      if (e.getTextBody() != null)
      {
         b.setText(e.getTextBody());
      }

      if (e.getHtmlBody() != null)
      {
         b.setHTML(e.getHtmlBody());
      }

      b.send();
   }

   public static Map<String, EmailAttachment> getEmailAttachmentMap(Collection<EmailAttachment> attachments)
   {
      Map<String, EmailAttachment> m = new HashMap<String, EmailAttachment>();

      for (EmailAttachment ea : attachments)
      {
         if (ea.getFileName() != null && ea.getFileName().length() > 0)
         {
            m.put(ea.getFileName(), ea);
         }
      }

      return null;
   }
}
