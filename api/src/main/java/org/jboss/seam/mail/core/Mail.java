package org.jboss.seam.mail.core;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.seam.mail.annotations.Velocity;
import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.templating.VelocityMailMessage;

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
   private Instance<MailMessage> mailMessage;

   /**
    * Create a new Standard Mail Message
    * 
    * @return new {@link StandardMailMessage}
    */
   public MailMessage standard()
   {
      return mailMessage.get();
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
