package org.jboss.seam.mail.example;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.mail.Session;

import org.jboss.logging.Logger;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.MailUtility;

public class GmailSessionProducer
{
   @Inject
   private Logger log;   


   @Gmail
   @Produces
   public Session getMailSession()
   {
      log.debug("Producing GMail Session");

      MailConfig mailConfig = new MailConfig();
      
      mailConfig.setServerHost("localhost");
      mailConfig.setServerPort(8978);
      mailConfig.setUsername("test");
      mailConfig.setPassword("test12!");
      mailConfig.setAuth(true);
     
      return MailUtility.buildMailSession(mailConfig);
   }
}
