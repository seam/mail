package org.jboss.seam.mail.util;


public class Strings
{
   public static boolean isNullOrBlank(String string)
   {     
      return string == null || string.trim().length() == 0;      
   }  
}
