package org.jboss.seam.mail.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


import org.jboss.seam.mail.core.EmailContact;
import org.jboss.seam.mail.core.enumurations.MessagePriority;

public class EmailMessage implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   private EmailContact fromAddress;
   private Collection<EmailContact> toAddresses = new ArrayList<EmailContact>();
   private Collection<EmailContact> ccAddresses = new ArrayList<EmailContact>();
   private Collection<EmailContact> bccAddresses = new ArrayList<EmailContact>();

   private String subject;
   private String textBody;
   private String htmlBody;

   private Collection<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
   private Collection<EmailAttachment> inlineAttachments = new ArrayList<EmailAttachment>();

   private Collection<String> deliveryReceiptAddresses = new ArrayList<String>();
   private Collection<String> readReceiptAddresses = new ArrayList<String>();

   private MessagePriority messagePriority;

   public EmailContact getFromAddress()
   {
      return fromAddress;
   }

   public void setFromAddress(EmailContact fromAddress)
   {
      this.fromAddress = fromAddress;
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

   public Collection<EmailAttachment> getAttachments()
   {
      return attachments;
   }

   public void setAttachments(Collection<EmailAttachment> attachments)
   {
      this.attachments = attachments;
   }

   public Collection<EmailAttachment> getInlineAttachments()
   {
      return inlineAttachments;
   }

   public void setInlineAttachments(Collection<EmailAttachment> inlineAttachments)
   {
      this.inlineAttachments = inlineAttachments;
   }

   public Collection<String> getDeliveryReceiptAddresses()
   {
      return deliveryReceiptAddresses;
   }

   public void setDeliveryReceiptAddresses(Collection<String> deliveryReceiptAddresses)
   {
      this.deliveryReceiptAddresses = deliveryReceiptAddresses;
   }

   public Collection<String> getReadReceiptAddresses()
   {
      return readReceiptAddresses;
   }

   public void setReadReceiptAddresses(Collection<String> readReceiptAddresses)
   {
      this.readReceiptAddresses = readReceiptAddresses;
   }

   public MessagePriority getMessagePriority()
   {
      return messagePriority;
   }

   public void setMessagePriority(MessagePriority messagePriority)
   {
      this.messagePriority = messagePriority;
   }
}

