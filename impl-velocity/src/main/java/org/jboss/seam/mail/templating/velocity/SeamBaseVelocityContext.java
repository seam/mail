/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.mail.templating.velocity;

import org.apache.velocity.VelocityContext;

/**
 * 
 * @author Cody Lerum
 * 
 */
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
