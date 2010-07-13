package org.jboss.seam.mail.core;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class RootMimeMessage extends MimeMessage
{   
   private String messageId;

   public RootMimeMessage(Session session)
   {
      super(session);
   }
   
   @Override
   protected void updateMessageID() throws MessagingException
   {
      setHeader("Message-ID", messageId);    
   }

   public String getMessageId()
   {
      return messageId;
   }

   public void setMessageId(String messageId)
   {
      this.messageId = messageId;
   }
}
