package org.jboss.seam.mail.core;

import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.mail.Session;

import org.slf4j.Logger;

import org.jboss.seam.mail.annotations.Module;

public class MailSessionProducer
{
   @Inject
   private Logger log;

   @Inject
   private MailConfig mailConfig;
   
   @Produces @Module
   public Session getMailSession()
   {
      log.debug("Producing Mail Session");
      Properties props = new Properties();
      props.put("mail.smtp.host", mailConfig.getServerHost());
      props.put("mail.smtp.port", mailConfig.getServerPort());
      return Session.getInstance(props, null);
   }
}
