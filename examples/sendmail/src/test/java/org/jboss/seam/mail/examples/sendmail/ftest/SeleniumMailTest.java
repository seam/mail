package org.jboss.seam.mail.examples.sendmail.ftest;


import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.locator.XpathLocator;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.jboss.test.selenium.locator.LocatorFactory.xp;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
/**
 * 
 * @author <a href="mailto:tremes@redhat.com"> Tomas Remes </a>
 *
 */
public class SeleniumMailTest extends AbstractTestCase
{

   public static final String HOME_PAGE_TITLE = "Seam 3 Sendmail Example";
   public static final String ENVELOPE_SENDER = "seam@test.test";

   public static final XpathLocator NAME = xp("//input[contains(@name,':name')]");
   public static final XpathLocator ADDRESS = xp("//input[contains(@name,':email')]");
   public static final String ADDRESS_VALUE= "john.smith@localhost";
   public static final String NAME_VALUE= "John Smith";
   
   public static final XpathLocator SEND_TEXT_EMAIL = xp("//input[contains(@value,'Send Text Email')]");
   public static final XpathLocator SEND_HTML_EMAIL_WITH_FREEMARKER = xp("//input[contains(@value,'Send HTML Email with FreeMarker')]");
   public static final XpathLocator SEND_HTML_EMAIL_WITH_VELOCITY = xp("//input[contains(@value,'Send HTML Email with Velocity')]");
   public static final XpathLocator SEND_HTML_EMAIL_WITH_SEAM = xp("//input[contains(@value,'Send HTML Email with Seam Render')]");
   public static final XpathLocator SEND_HTML_TEXT_EMAIL_WITH_FREEMARKER =  xp("//input[contains(@value,'Send HTML+Text Email with FreeMarker')]");
   public static final XpathLocator SEND_HTML_TEXT_EMAIL_WITH_VELOCITY =  xp("//input[contains(@value,'Send HTML+Text Email with Velocity')]");
   public static final XpathLocator SEND_HTML_TEXT_EMAIL_WITH_SEAM =  xp("//input[contains(@value,'Send HTML+Text Email with Seam Render')]");
   
   protected Wiser wiser;
  
  
   @BeforeMethod
   public void setUp() throws MalformedURLException
   {
	   selenium.setSpeed(300);
       selenium.open(new URL(contextPath.toString()));
    }

   /**
    * We restart SMTP after each Method, because Wiser doesn't have mechanism to flush recieved emails.
    */
   @BeforeMethod
   public void startSMTP() 
   {
      wiser = new Wiser();
      wiser.setPort(8977);
      wiser.start();
      
   }

   @AfterMethod
   public void stopSMTP()
   {
      wiser.stop();
   }
   
   /**
    * Place holder - just verifies that example deploys
    */
   @Test
   public void homePageLoadTest()
   {
      assertEquals("Unexpected page title.", HOME_PAGE_TITLE, selenium.getTitle());
   }
   
   @DataProvider(name = "sendMethods")
   public Object[][] mailTest() {
	   
      return new Object[][] {
            {SEND_TEXT_EMAIL, new String[] {"Content-Type: text/plain","Simple Message with text body"}},
            {SEND_HTML_EMAIL_WITH_FREEMARKER, new String[] {"Content-Type: text/html","Content-Type: multipart/mixed","Content-Type: image/png; name=seamLogo.png","Content-Disposition: inline","<p><b>Dear <a href=\"mailto:"+ ADDRESS_VALUE+"\">"+NAME_VALUE+"</a>,</b></p>","<p>This is an example <i>HTML</i> email sent by Seam 3 and FreeMarker.</p>","Importance: high"}},
            {SEND_HTML_EMAIL_WITH_VELOCITY, new String[]{"Content-Type: text/html","Content-Type: multipart/mixed","Content-Type: image/png; name=seamLogo.png","Content-Disposition: inline","<p><b>Dear <a href=\"mailto:"+ ADDRESS_VALUE+"\">"+NAME_VALUE+"</a>,</b></p>","<p>This is an example <i>HTML</i> email sent by Seam 3 and Velocity.</p>","Importance: high"}},
            {SEND_HTML_EMAIL_WITH_SEAM, new String[] {"Content-Type: text/html","Content-Type: multipart/mixed","Content-Type: image/png; name=seamLogo.png","Content-Disposition: inline","<p><b>Dear <a href=\"mailto:"+ ADDRESS_VALUE+"\">"+NAME_VALUE+"</a>,</b></p>","<p>This is an example <i>HTML</i> email sent by Seam 3 and Seam Render.</p>","Importance: high"}},
            {SEND_HTML_TEXT_EMAIL_WITH_FREEMARKER, new String[] {"Content-Type: text/plain","Content-Type: multipart/alternative","Content-Type: multipart/mixed","Content-Type: image/png; name=seamLogo.png","Content-Disposition: inline","Hello "+NAME_VALUE,"This is the alternative text body for mail readers that don't support html. This was sent with Seam 3 and FreeMarker.","Importance: low"}},
            {SEND_HTML_TEXT_EMAIL_WITH_VELOCITY, new String[] {"Content-Type: text/plain","Content-Type: multipart/alternative","Content-Type: multipart/mixed","Content-Type: image/png; name=seamLogo.png","Content-Disposition: inline","Hello "+NAME_VALUE,"This is the alternative text body for mail readers that don't support html. This was sent with Seam 3 and Velocity.","Importance: low"}},
            {SEND_HTML_TEXT_EMAIL_WITH_SEAM, new String[] {"Content-Type: text/plain","Content-Type: multipart/alternative","Content-Type: multipart/mixed","Content-Type: image/png; name=seamLogo.png","Content-Disposition: inline","Hello "+NAME_VALUE,"This is the alternative text body for mail readers that don't support html. This was sent with Seam 3 and Seam Render.","Importance: low"}},

      };
   }
   
   /**
    * Sends a mail and verifies it was delivered
    */
   @Test(dataProvider = "sendMethods")
   public void mailTest(XpathLocator buttonToClick, String[] expectedMessageContents) {

	  fillInInputs();
      sendEmail(buttonToClick);
      checkDelivered(expectedMessageContents);
   }

   /**
    * Fills in html text inputs.
    */
   private void fillInInputs()
   {
      selenium.type(NAME, NAME_VALUE);
      selenium.type(ADDRESS, ADDRESS_VALUE);
     
   }
   
   /**
    * Sends an email by clicking on specified button.
    * @param buttonToClick
    */
   private void sendEmail(XpathLocator buttonToClick)
   {
	   waitHttp(selenium).click(buttonToClick);
	}   
   /**
    * Checks that the expected email was delivered.
    * @param expectedMessageContents
    */
   private void checkDelivered(String[] expectedMessageContents)
   {
	  assertFalse("Expected a message", wiser.getMessages().isEmpty());
      WiserMessage message = wiser.getMessages().get(0); // although "send plain text" example sends 3 mails (To:, CC:, Bcc:) Wiser cannot distinquish between them so we just check the first mail.
      assertEquals(ADDRESS_VALUE, message.getEnvelopeReceiver());
      assertTrue("Envelope sender (" + message.getEnvelopeSender() + ") doesn't match expected one (" + ENVELOPE_SENDER + ")", message.getEnvelopeSender().matches(ENVELOPE_SENDER));
      
      for (String expectedMessageContent: expectedMessageContents) {
         assertTrue("Didn't find expected text (" + expectedMessageContent + ") in the received email.", new String(message.getData()).contains(expectedMessageContent));
      }
   }

}
