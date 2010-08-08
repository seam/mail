package org.jboss.seam.mail.core;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailSessionAuthenticator extends Authenticator
{
   private String username;
   private String password;
   
   public MailSessionAuthenticator(String username, String password)
   {
      this.username = username;
      this.password = password;
   }
   
   @Override
   protected PasswordAuthentication getPasswordAuthentication()
   {
       return new PasswordAuthentication(username, password);
   }
}
