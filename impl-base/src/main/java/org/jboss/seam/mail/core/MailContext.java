package org.jboss.seam.mail.core;

import java.util.Map;

public class MailContext
{
   
   private Map<String, Attachment> attachments;
   
   public MailContext(Map<String, Attachment> attachments)
   {
      this.attachments = attachments;
   }
   
   public String insert(String fileName)
   {
      Attachment attachment = null;     

      attachment = attachments.get(fileName);     
      
      if(attachment == null)
      {
         throw new RuntimeException("Unable to find attachment: " + fileName);
      }
      else
      {
         return "cid:" + attachment.getId();
      }      
   }
}
