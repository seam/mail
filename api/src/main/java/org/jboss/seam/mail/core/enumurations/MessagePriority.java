package org.jboss.seam.mail.core.enumurations;

public enum MessagePriority
{
   LOW("5", "non-urgent", "low"),
   HIGH("1", "urgent", "high");
   
   private String x_priority;
   private String priority;
   private String importance;
   
   private MessagePriority(String x_priority, String priority, String importance)
   {
      this.x_priority = x_priority;
      this.priority = priority;
      this.importance = importance;
   }

   public String getX_priority()
   {
      return x_priority;
   }

   public String getPriority()
   {
      return priority;
   }

   public String getImportance()
   {
      return importance;
   }
}
