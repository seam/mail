package org.jboss.seam.mail.core.enumurations;

public enum MessagePriority
{
   LOW("5", "non-urgent", "low"),
   NORMAL("3", "normal", "normal"),
   HIGH("1", "urgent", "high");
   
   private String xPriority;
   private String priority;
   private String importance;
   
   private MessagePriority(String x_priority, String priority, String importance)
   {
      this.xPriority = x_priority;
      this.priority = priority;
      this.importance = importance;
   }

   public String getX_priority()
   {
      return xPriority;
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
