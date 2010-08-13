package org.jboss.seam.mail.core;

import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.mail.Session;

import org.slf4j.Logger;

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
      
      Session session;
      
      Properties props = new Properties();
      props.put("mail.smtp.host", mailConfig.getServerHost());
      props.put("mail.smtp.port", mailConfig.getServerPort());
      
      if(mailConfig.getUsername() != null && 
            mailConfig.getUsername().length() != 0 &&
            mailConfig.getPassword() != null && mailConfig.getPassword().length() != 0)
      {
         MailSessionAuthenticator authenticator = new MailSessionAuthenticator(mailConfig.getUsername(), mailConfig.getPassword());
         
         session = Session.getInstance(props, authenticator);
      }
      else
      {
         session =  Session.getInstance(props, null);
      }
      return session;
   }
}
