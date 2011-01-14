package org.jboss.seam.mail.util;

import org.subethamail.smtp.auth.LoginFailedException;
import org.subethamail.smtp.auth.UsernamePasswordValidator;

public class SMTPAuthenticator implements UsernamePasswordValidator
{
   private String username;
   private String password;

   public SMTPAuthenticator(String username, String password)
   {
      this.username = username;
      this.password = password;
   }

   @Override
   public void login(String username, String password) throws LoginFailedException
   {
      if (this.username.equals(username) && this.password.equals(password))
      {
         return;
      }
      else
      {
         throw new LoginFailedException();
      }
   }

}
