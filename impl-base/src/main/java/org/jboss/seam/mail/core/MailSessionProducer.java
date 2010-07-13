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
   private MailConfig config;
   
   @Produces @Module
   public Session getMailSession()
   {
      log.debug("Producing Mail Session");
      Properties props = new Properties();
      props.put("mail.smtp.host", config.getServerHost());
      return Session.getInstance(props, null);
   }
}
