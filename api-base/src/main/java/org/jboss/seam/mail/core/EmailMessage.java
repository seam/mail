package org.jboss.seam.mail.core;

import java.util.ArrayList;
import java.util.Collection;

import org.jboss.seam.mail.core.enumurations.MessagePriority;

public class EmailMessage
{
   private String rootSubType = "mixed";
   private EmailMessageType type = EmailMessageType.STANDARD;
   private String messageId;
   private String lastMessageId;
   private Collection<EmailContact> fromAddresses = new ArrayList<EmailContact>();
   private Collection<EmailContact> replyToAddresses = new ArrayList<EmailContact>();
   private Collection<EmailContact> toAddresses = new ArrayList<EmailContact>();
   private Collection<EmailContact> ccAddresses = new ArrayList<EmailContact>();
   private Collection<EmailContact> bccAddresses = new ArrayList<EmailContact>();
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
   
   public Collection<EmailContact> getFromAddresses()
   {
      return fromAddresses;
   }

   public void addFromAddress(EmailContact fromAddress)
   {
      this.fromAddresses.add(fromAddress);
   }

   public void addFromAddresses(Collection<EmailContact> fromAddresses)
   {
      this.fromAddresses.addAll(fromAddresses);
   }

   public Collection<EmailContact> getReplyToAddresses()
   {
      return replyToAddresses;
   }

   public void addReplyToAddress(EmailContact replyToAddress)
   {
      this.replyToAddresses.add(replyToAddress);
   }

   public void addReplyToAddresses(Collection<EmailContact> replyToAddress)
   {
      this.replyToAddresses.addAll(replyToAddress);
   }

   public Collection<EmailContact> getToAddresses()
   {
      return toAddresses;
   }

   public void addToAddress(EmailContact toAddress)
   {
      this.toAddresses.add(toAddress);
   }

   public void addToAddresses(Collection<EmailContact> toAddresses)
   {
      this.toAddresses.addAll(toAddresses);
   }

   public boolean removeToAddress(EmailContact toAddress)
   {
      return toAddresses.remove(toAddress);
   }

   public Collection<EmailContact> getCcAddresses()
   {
      return ccAddresses;
   }

   public void addCcAddress(EmailContact ccAddress)
   {
      this.ccAddresses.add(ccAddress);
   }

   public void addCcAddresses(Collection<EmailContact> ccAddresses)
   {
      this.ccAddresses.addAll(ccAddresses);
   }

   public boolean removeCcAddress(EmailContact ccAddress)
   {
      return ccAddresses.remove(ccAddress);
   }

   public Collection<EmailContact> getBccAddresses()
   {
      return bccAddresses;
   }

   public void addBccAddress(EmailContact bccAddress)
   {
      this.bccAddresses.add(bccAddress);
   }

   public void addBccAddresses(Collection<EmailContact> bccAddresses)
   {
      this.bccAddresses.addAll(bccAddresses);
   }

   public boolean removeBccAddress(EmailContact bccAddress)
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
