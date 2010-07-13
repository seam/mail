package org.jboss.seam.mail;

import javax.enterprise.inject.Model;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Model
public class Person
{
   private String name;
   private String email;

   public Person()
   {
      
   }
   
   public Person(String name, String email)
   {
      this.name = name;
      this.email = email;
   }

   @NotNull
   @NotEmpty
   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   @NotNull
   @NotEmpty
   @Email
   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }
}
