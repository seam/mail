package org.jboss.seam.mail.core;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeUtility;

/**
 * 
 * @author Cody Lerum
 * 
 */
public class Header implements Serializable
{
   private static final long serialVersionUID = 1L;

   private String name;
   private String value;

   public Header(String name, String value)
   {
      this.name = name;

      try
      {
         this.value = MimeUtility.fold(name.length() + 2, MimeUtility.encodeText(value));
      }
      catch (UnsupportedEncodingException e)
      {
         throw new RuntimeException("Unable to create header", e);
      }
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }
}
