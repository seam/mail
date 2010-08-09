package org.jboss.seam.mail.core;

import javax.enterprise.context.ApplicationScoped;

/**
 * Bean which holds Mail Session configuraiton options. Can be configured via
 * Seam XML
 * 
 * @author Cody Lerum
 * 
 */
@ApplicationScoped
public class MailConfig
{
   private String serverHost = "localhost";
   private int serverPort = 25;
   private String username;
   private String password;
   private boolean tls = true;
   private boolean ssl = false;


   public String getServerHost()
   {
      return serverHost;
   }

   public void setServerHost(String serverHost)
   {
      this.serverHost = serverHost;
   }

   public int getServerPort()
   {
      return serverPort;
   }

   public void setServerPort(int serverPort)
   {
      this.serverPort = serverPort;
   }

   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   public boolean isTls()
   {
      return tls;
   }

   public void setTls(boolean tls)
   {
      this.tls = tls;
   }

   public boolean isSsl()
   {
      return ssl;
   }

   public void setSsl(boolean ssl)
   {
      this.ssl = ssl;
   }
}
