package org.jboss.seam.mail.core;

/**
 * 
 * @author Cody Lerum
 * 
 */
public enum ContentType
{
   ALTERNATIVE("alternative"),
   MIXED("mixed"),
   RELATED("related");

   private String value;

   private ContentType(String value)
   {
      this.value = value;
   }

   public String getValue()
   {
      return value;
   }
}
