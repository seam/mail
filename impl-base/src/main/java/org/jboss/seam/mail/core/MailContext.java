package org.jboss.seam.mail.core;

import java.util.Map;

public class MailContext
{
   
   private Map<String, AttachmentPart> attachments;
   
   public MailContext(Map<String, AttachmentPart> attachments)
   {
      this.attachments = attachments;
   }
   
   public String insert(String fileName)
   {
      AttachmentPart attachment = null;     

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
