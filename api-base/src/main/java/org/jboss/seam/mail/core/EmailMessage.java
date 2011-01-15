package org.jboss.seam.mail.core;

import java.util.ArrayList;
import java.util.Collection;

import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.core.enumurations.MessagePriority;

public class EmailMessage
{
   private String rootSubType = "mixed";
   private EmailMessageType type = EmailMessageType.STANDARD;
   private String messageId;
   private String lastMessageId;
   private Collection<InternetAddress> fromAddresses = new ArrayList<InternetAddress>();
   private Collection<InternetAddress> replyToAddresses = new ArrayList<InternetAddress>();
   private Collection<InternetAddress> toAddresses = new ArrayList<InternetAddress>();
   private Collection<InternetAddress> ccAddresses = new ArrayList<InternetAddress>();
   private Collection<InternetAddress> bccAddresses = new ArrayList<InternetAddress>();
   private Collection<Header> headers = new ArrayList<Header>();

   private String subject;
   private String textBody;
   private String htmlBody;

   private Collection<EmailAttachment> attachments = new ArrayList<EmailAttachment>();

   private Collection<String> deliveryReceiptAddresses = new ArrayList<String>();
   private Collection<String> readReceiptAddresses = new ArrayList<String>();

   private MessagePriority importance;

   public EmailMessage()
   {

   }

   public EmailMessage(String rootSubType)
   {
      this.rootSubType = rootSubType;
   }

   public String getRootSubType()
   {
      return rootSubType;
   }

   public void setRootSubType(String rootSubType)
   {
      this.rootSubType = rootSubType;
   }

   public EmailMessageType getType()
   {
      return type;
   }

   public void setType(EmailMessageType type)
   {
      this.type = type;
   }

   public String getMessageId()
   {
      return messageId;
   }

   public void setMessageId(String messageId)
   {
      this.messageId = messageId;
   }

   public String getLastMessageId()
   {
      return lastMessageId;
   }

   public void setLastMessageId(String lastMessageId)
   {
      this.lastMessageId = lastMessageId;
   }
   
   public Collection<InternetAddress> getFromAddresses()
   {
      return fromAddresses;
   }

   public void addFromAddress(InternetAddress fromAddress)
   {
      this.fromAddresses.add(fromAddress);
   }

   public void addFromAddresses(Collection<InternetAddress> fromAddresses)
   {
      this.fromAddresses.addAll(fromAddresses);
   }

   public Collection<InternetAddress> getReplyToAddresses()
   {
      return replyToAddresses;
   }

   public void addReplyToAddress(InternetAddress replyToAddress)
   {
      this.replyToAddresses.add(replyToAddress);
   }

   public void addReplyToAddresses(Collection<InternetAddress> replyToAddress)
   {
      this.replyToAddresses.addAll(replyToAddress);
   }

   public Collection<InternetAddress> getToAddresses()
   {
      return toAddresses;
   }

   public void addToAddress(InternetAddress toAddress)
   {
      this.toAddresses.add(toAddress);
   }

   public void addToAddresses(Collection<InternetAddress> toAddresses)
   {
      this.toAddresses.addAll(toAddresses);
   }

   public boolean removeToAddress(InternetAddress toAddress)
   {
      return toAddresses.remove(toAddress);
   }

   public Collection<InternetAddress> getCcAddresses()
   {
      return ccAddresses;
   }

   public void addCcAddress(InternetAddress ccAddress)
   {
      this.ccAddresses.add(ccAddress);
   }

   public void addCcAddresses(Collection<InternetAddress> ccAddresses)
   {
      this.ccAddresses.addAll(ccAddresses);
   }

   public boolean removeCcAddress(InternetAddress ccAddress)
   {
      return ccAddresses.remove(ccAddress);
   }

   public Collection<InternetAddress> getBccAddresses()
   {
      return bccAddresses;
   }

   public void addBccAddress(InternetAddress bccAddress)
   {
      this.bccAddresses.add(bccAddress);
   }

   public void addBccAddresses(Collection<InternetAddress> bccAddresses)
   {
      this.bccAddresses.addAll(bccAddresses);
   }

   public boolean removeBccAddress(InternetAddress bccAddress)
   {
      return bccAddresses.remove(bccAddress);
   }

   public Collection<Header> getHeaders()
   {
      return headers;
   }
   
   public void addHeader(Header header)
   {
      headers.add(header);
   }

   public void addHeaders(Collection<Header> headers)
   {
      this.headers.addAll(headers);
   }

   public String getSubject()
   {
      return subject;
   }

   public void setSubject(String subject)
   {
      this.subject = subject;
   }

   public String getTextBody()
   {
      return textBody;
   }

   public void setTextBody(String textBody)
   {
      this.textBody = textBody;
   }

   public String getHtmlBody()
   {
      return htmlBody;
   }

   public void setHtmlBody(String htmlBody)
   {
      this.htmlBody = htmlBody;
   }

   public Collection<String> getDeliveryReceiptAddresses()
   {
      return deliveryReceiptAddresses;
   }

   public void addDeliveryReceiptAddress(String address)
   {
      deliveryReceiptAddresses.add(address);
   }

   public void addDeliveryReceiptAddresses(Collection<String> deliveryReceiptAddresses)
   {
      deliveryReceiptAddresses.addAll(deliveryReceiptAddresses);
   }

   public Collection<String> getReadReceiptAddresses()
   {
      return readReceiptAddresses;
   }

   public void addReadReceiptAddress(String address)
   {
      readReceiptAddresses.add(address);
   }

   public void addReadReceiptAddresses(Collection<String> readReceiptAddresses)
   {
      readReceiptAddresses.addAll(readReceiptAddresses);
   }

   public MessagePriority getImportance()
   {
      return importance;
   }

   public void setImportance(MessagePriority importance)
   {
      this.importance = importance;
   }

   public void addAttachment(EmailAttachment attachment)
   {
      attachments.add(attachment);
   }

   public void addAttachments(Collection<EmailAttachment> attachments)
   {
      for (EmailAttachment e : attachments)
      {
         addAttachment(e);
      }
   }

   public Collection<EmailAttachment> getAttachments()
   {
      return attachments;
   }
}
