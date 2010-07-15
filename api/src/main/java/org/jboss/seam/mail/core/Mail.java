package org.jboss.seam.mail.core;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.seam.mail.annotations.Velocity;
import org.jboss.seam.mail.api.StandardMailMessage;
import org.jboss.seam.mail.api.VelocityMailMessage;

/**
 * Method to create new MailMessage instances
 * 
 * @author Cody Lerum
 * 
 */
public class Mail
{
   @Inject
   @Velocity
   private Instance<VelocityMailMessage> velocityMailMessage;

   @Inject
   private Instance<StandardMailMessage> standardMailMessage;

   /**
    * Create a new Standard Mail Message
    * 
    * @return new {@link StandardMailMessage}
    */
   public StandardMailMessage standard()
   {
      return standardMailMessage.get();
   }

   /**
    * Create a new VelocityMailMessage
    * 
    * @return new {@link VelocityMailMessage}
    */
   public VelocityMailMessage velocity()
   {
      return velocityMailMessage.get();
   }
}
