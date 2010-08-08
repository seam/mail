package org.jboss.seam.mail.templating.velocity;

import org.apache.velocity.VelocityContext;
import org.jboss.seam.mail.exception.SeamMailException;

public class SeamBaseVelocityContext extends VelocityContext
{
   private VelocityMailMessageImpl mailMessage;
   
   public SeamBaseVelocityContext(VelocityMailMessageImpl velocityMailMessageImpl, SeamCDIVelocityContext seamCDIVelocityContext)
   {
      super(seamCDIVelocityContext);
      this.mailMessage = velocityMailMessageImpl;
   }
   
   public void to(String name, String address) throws SeamMailException
   {
      mailMessage.to(name, address);
   }
   
   public void from(String name, String address) throws SeamMailException
   {
      mailMessage.from(name, address);
   }
   
   public void subject(String subject) throws SeamMailException
   {
      mailMessage.subject(subject);
   }
}
