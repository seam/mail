package org.jboss.seam.mail.util;

public class Strings
{
   public static boolean isNullOrEmpty(String string, boolean trim)
   {
      if(trim)
      {
         return string == null || string.trim().length() == 0;
      }
      else
      {
         return string == null || string.length() == 0;
      }
   }
   
   public static boolean isNullOrEmpty(String string)
   {
      return isNullOrEmpty(string, false);
   }
}
