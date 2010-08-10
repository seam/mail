package org.jboss.seam.mail.core.enumurations;

public enum RecipientType
{
   TO(javax.mail.Message.RecipientType.TO),
   CC(javax.mail.Message.RecipientType.CC),
   BCC(javax.mail.Message.RecipientType.BCC);
   
   private javax.mail.Message.RecipientType recipientType;
   
   private RecipientType(javax.mail.Message.RecipientType recipientType)
   {
      this.recipientType = recipientType;
   }

   public javax.mail.Message.RecipientType getRecipientType()
   {
      return recipientType;
   }
}
