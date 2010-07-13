package org.jboss.seam.mail.core;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.seam.mail.annotations.Velocity;
import org.jboss.seam.mail.api.StandardMailMessage;
import org.jboss.seam.mail.api.VelocityMailMessage;

public class Mail
{
   @Inject @Velocity 
   private Instance<VelocityMailMessage> velocityMailMessage; 
   
   @Inject 
   private Instance<StandardMailMessage> standardMailMessage;

   public StandardMailMessage standard()
   {       
      return standardMailMessage.get();
   }

   public VelocityMailMessage velocity()
   {
      return velocityMailMessage.get();
   }
}
