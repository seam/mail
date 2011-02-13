package org.jboss.seam.mail.core;


/**
 * 
 * @author Cody Lerum
 * 
 */
public class BasicEmailContact implements EmailContact
{
   private static final long serialVersionUID = 1L;

   private String address;
   private String name;

   public BasicEmailContact(String address)
   {
      this.address = address;
   }

   public BasicEmailContact(String address, String name)
   {
      this.address = address;
      this.name = name;
   }

   public String getAddress()
   {
      return address;
   }

   public void setAddress(String address)
   {
      this.address = address;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }
}
