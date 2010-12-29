package org.jboss.seam.mail.example;

import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.mail.Session;

import org.jboss.logging.Logger;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.MailSessionAuthenticator;

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
      Session session;

      Properties props = new Properties();
      props.put("mail.smtp.host", mailConfig.getServerHost());
      props.put("mail.smtp.port", mailConfig.getServerPort());

      if (mailConfig.isAuth() && mailConfig.getUsername() != null && mailConfig.getPassword() != null)
      {      
         props.put("mail.smtp.auth", "true");
         
         MailSessionAuthenticator authenticator = new MailSessionAuthenticator(mailConfig.getUsername(), mailConfig.getPassword());

         session = Session.getInstance(props, authenticator);
      }
      else
      {
         session = Session.getInstance(props, null);
      }
      
      return session;
   }
}
