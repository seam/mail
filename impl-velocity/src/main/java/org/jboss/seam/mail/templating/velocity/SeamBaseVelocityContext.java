package org.jboss.seam.mail.templating.velocity;

import org.apache.velocity.VelocityContext;

public class SeamBaseVelocityContext extends VelocityContext
{
   private VelocityMailMessageImpl mailMessage;
   
   public SeamBaseVelocityContext(VelocityMailMessageImpl velocityMailMessageImpl, SeamCDIVelocityContext seamCDIVelocityContext)
   {
      super(seamCDIVelocityContext);
      this.mailMessage = velocityMailMessageImpl;
   }
   
   public void to(String name, String address)
   {
      mailMessage.to(name, address);
   }
   
   public void from(String name, String address)
   {
      mailMessage.from(name, address);
   }
   
   public void subject(String subject)
   {
      mailMessage.subject(subject);
   }
}
