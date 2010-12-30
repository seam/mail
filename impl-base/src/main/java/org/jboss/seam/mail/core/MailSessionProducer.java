package org.jboss.seam.mail.core;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.mail.Session;

import org.jboss.logging.Logger;

public class MailSessionProducer
{
   @Inject
   private Logger log;

   @Inject
   private MailConfig mailConfig;

   @Produces
   public Session getMailSession()
   {
      log.debug("Producing Mail Session");

      return MailUtility.buildMailSession(mailConfig);
      
   }
}
