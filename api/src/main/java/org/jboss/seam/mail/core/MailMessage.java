package org.jboss.seam.mail.core;

import java.io.File;
import java.net.URL;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.core.enumurations.RecipientType;
import org.jboss.seam.mail.exception.SeamMailException;

public interface MailMessage<T extends MailMessage<T>>
{
   public T setFrom(EmailContact emailContact) throws SeamMailException;

   public T addRecipient(RecipientType recipientType, EmailContact emailContact) throws SeamMailException;

   public T from(String name, String address) throws SeamMailException;

   public T to(String name, String address) throws SeamMailException;

   public T cc(String name, String address) throws SeamMailException;

   public T bcc(String name, String address) throws SeamMailException;

   public T subject(String string) throws SeamMailException;

   public T setText(String text) throws SeamMailException;

   public T setHTML(String text) throws SeamMailException;

   public T setHTMLTextAlt(String html, String text) throws SeamMailException;

   public T importance(MessagePriority messagePriority) throws SeamMailException;

   public T addAttachment(File file, ContentDisposition attachment) throws SeamMailException;
   
   public T addAttachment(String fileName, ContentDisposition attachment) throws SeamMailException;
   
   public T addAttachment(String fileName, String mimeType, ContentDisposition attachment) throws SeamMailException;

   public T addAttachment(URL url, String fileName, ContentDisposition contentDisposition) throws SeamMailException;

   public T deliveryReciept(String address) throws SeamMailException;

   public T readReciept(String string) throws SeamMailException;

   public void send() throws SeamMailException;

}
