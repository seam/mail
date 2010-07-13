package org.jboss.seam.mail.core;

import java.util.Map;

import org.jboss.seam.mail.exception.SeamMailException;

public class AttachmentMap
{
   
   private Map<String, Attachment> attachments;
   
   public AttachmentMap(Map<String, Attachment> attachments)
   {
      this.attachments = attachments;
   }
   
   public String insert(String fileName) throws SeamMailException
   {
      Attachment attachment = null;     

      attachment = attachments.get(fileName);     
      
      if(attachment == null)
      {
         throw new SeamMailException("Unable to find attachment: " + fileName);
      }
      else
      {
         return "cid:" + attachment.getId();
      }      
   }
}
