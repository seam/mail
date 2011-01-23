package org.jboss.mail.core.test;

import java.util.ArrayList;
import java.util.Collection;

import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.InvalidAddressException;
import org.jboss.seam.mail.core.MailMessageImpl;
import org.jboss.seam.mail.core.MailUtility;
import org.junit.Test;

public class InternetAddressTest
{
   @Test
   public void validAddresses()
   {
      MailMessage m = new MailMessageImpl();

      InternetAddress seam = MailUtility.internetAddress("seam@domain.test");
      InternetAddress seamey = MailUtility.internetAddress("seamey@domain.test");

      Collection<InternetAddress> addresses = new ArrayList<InternetAddress>();
      addresses.add(seamey);

      m.from("Seam Seamerson", "seam@domain.test");
      m.from("seam@domain.test");
      m.from(seam);
      m.from(addresses);

      m.to("Seam Seamerson", "seam@domain.test");
      m.to("seam@domain.test");
      m.to(seam);
      m.to(addresses);

      m.cc("Seam Seamerson", "seam@domain.test");
      m.cc("seam@domain.test");
      m.cc(seam);
      m.cc(addresses);

      m.bcc("Seam Seamerson", "seam@domain.test");
      m.bcc("seam@domain.test");
      m.bcc(seam);
      m.bcc(addresses);

      m.replyTo("Seam Seamerson", "seam@domain.test");
      m.replyTo("seam@domain.test");
      m.replyTo(seam);
      m.replyTo(addresses);
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidFromSimpleAddresses()
   {
      MailMessage m = new MailMessageImpl();

      m.from("woo foo @bar.com");
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidFromFullAddresses()
   {
      MailMessage m = new MailMessageImpl();

      m.from("Woo", "foo @bar.com");
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidToSimpleAddresses()
   {
      MailMessage m = new MailMessageImpl();

      m.to("woo foo @bar.com");
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidToFullAddresses()
   {
      MailMessage m = new MailMessageImpl();

      m.to("Woo", "foo @bar.com");
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidCcSimpleAddresses()
   {
      MailMessage m = new MailMessageImpl();

      m.cc("woo foo @bar.com");
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidCcFullAddresses()
   {
      MailMessage m = new MailMessageImpl();

      m.cc("Woo", "foo @bar.com");
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidBccSimpleAddresses()
   {
      MailMessage m = new MailMessageImpl();

      m.bcc("woo foo @bar.com");
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidbccFullAddresses()
   {
      MailMessage m = new MailMessageImpl();

      m.bcc("Woo", "foo @bar.com");
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidReplyToSimpleAddresses()
   {
      MailMessage m = new MailMessageImpl();

      m.replyTo("woo foo @bar.com");
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidReplyToFullAddresses()
   {
      MailMessage m = new MailMessageImpl();

      m.replyTo("Woo", "foo @bar.com");
   }
   
   @Test(expected = InvalidAddressException.class)
   public void invalidDeliveryReceipt()
   {
      MailMessage m = new MailMessageImpl();

      m.deliveryReceipt("woo foo @bar.com");
   }

   @Test(expected = InvalidAddressException.class)
   public void invalidReadReceipt()
   {
      MailMessage m = new MailMessageImpl();

      m.readReceipt("woo foo @bar.com");
   }
}
