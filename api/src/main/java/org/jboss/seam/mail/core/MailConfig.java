package org.jboss.seam.mail.core;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MailConfig
{
   private String serverHost = "localhost";
   private int serverPort = 2525;

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
}
