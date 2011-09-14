package org.jboss.seam.mail.examples.sendmail.ftest;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.locator.XPathLocator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.seam.mail.example.SendMail;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import static org.jboss.arquillian.ajocado.Ajocado.waitForHttp;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.xp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * A functional test for send mail example
 *
 * @author <a href="mailto:tremes@redhat.com"> Tomas Remes </a>
 */
@RunAsClient
@RunWith(Arquillian.class)
public class SeleniumMailTest {
    
    public static final String ARCHIVE_NAME = "mail-sendmail.war";
    public static final String BUILD_DIRECTORY = "target";

    public static final String HOME_PAGE_TITLE = "Seam 3 Sendmail Example";
    public static final String ENVELOPE_SENDER = "seam@test.test";

    public static final XPathLocator NAME = xp("//input[contains(@name,':name')]");
    public static final XPathLocator ADDRESS = xp("//input[contains(@name,':email')]");
    public static final String ADDRESS_VALUE = "john.smith@localhost";
    public static final String NAME_VALUE = "John Smith";

    public static final XPathLocator SEND_TEXT_EMAIL = xp("//input[contains(@value,'Send Text Email')]");
    public static final XPathLocator SEND_HTML_EMAIL_WITH_FREEMARKER = xp("//input[contains(@value,'Send HTML Email with FreeMarker')]");
    public static final XPathLocator SEND_HTML_EMAIL_WITH_VELOCITY = xp("//input[contains(@value,'Send HTML Email with Velocity')]");
    public static final XPathLocator SEND_HTML_EMAIL_WITH_SEAM = xp("//input[contains(@value,'Send HTML Email with Seam Render')]");
    public static final XPathLocator SEND_HTML_TEXT_EMAIL_WITH_FREEMARKER = xp("//input[contains(@value,'Send HTML+Text Email with FreeMarker')]");
    public static final XPathLocator SEND_HTML_TEXT_EMAIL_WITH_VELOCITY = xp("//input[contains(@value,'Send HTML+Text Email with Velocity')]");
    public static final XPathLocator SEND_HTML_TEXT_EMAIL_WITH_SEAM = xp("//input[contains(@value,'Send HTML+Text Email with Seam Render')]");

    private Wiser wiser;

    @Drone
    private AjaxSelenium selenium;

    @ArquillianResource
    private URL deploymentUrl;
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(ZipImporter.class, ARCHIVE_NAME).importFrom(new File(BUILD_DIRECTORY + '/' + ARCHIVE_NAME))
                .as(WebArchive.class);
    }


    /**
     * We restart SMTP after each Method, because Wiser doesn't have mechanism to flush recieved emails.
     */
    @Before
    public void setUp() throws MalformedURLException {
        wiser = new Wiser();
        wiser.setPort(8977);
        wiser.start();
        
        selenium.open(deploymentUrl);
        selenium.waitForPageToLoad();
    }

    @After
    public void stopSMTP() {
        wiser.stop();
    }

    /**
     * Place holder - just verifies that example deploys
     */
    @Test
    public void homePageLoadTest() {
        assertEquals("Unexpected page title.", HOME_PAGE_TITLE, selenium.getTitle());
    }

    /**
     * Sends a mail and verifies it was delivered
     */
    @Test
    public void textMailTest() {
        fillInInputs();
        sendEmail(SEND_TEXT_EMAIL);
        checkDelivered(new String[]{"Content-Type: text/plain", "Simple Message with text body"});
    }

    @Test
    public void velocityHtmlMailTest() {
        fillInInputs();
        sendEmail(SEND_HTML_EMAIL_WITH_VELOCITY);
        checkDelivered(new String[]{"Content-Type: text/html", "Content-Type: multipart/mixed", "Content-Type: image/png; name=seamLogo.png", "Content-Disposition: inline", "<p><b>Dear <a href=\"mailto:" + ADDRESS_VALUE + "\">" + NAME_VALUE + "</a>,</b></p>", "<p>This is an example <i>HTML</i> email sent by Seam 3 and Velocity.</p>", "Importance: high"});
    }

    @Test
    public void freemarkerHtmlMailTest() {
        fillInInputs();
        sendEmail(SEND_HTML_EMAIL_WITH_FREEMARKER);
        checkDelivered(new String[]{"Content-Type: text/html", "Content-Type: multipart/mixed", "Content-Type: image/png; name=seamLogo.png", "Content-Disposition: inline", "<p><b>Dear <a href=\"mailto:" + ADDRESS_VALUE + "\">" + NAME_VALUE + "</a>,</b></p>", "<p>This is an example <i>HTML</i> email sent by Seam 3 and FreeMarker.</p>", "Importance: high"});
    }

    @Test
    public void seamHtmlMailTest() {
        fillInInputs();
        sendEmail(SEND_HTML_EMAIL_WITH_SEAM);
        checkDelivered(new String[]{"Content-Type: text/html", "Content-Type: multipart/mixed", "Content-Type: image/png; name=seamLogo.png", "Content-Disposition: inline", "<p><b>Dear <a href=\"mailto:" + ADDRESS_VALUE + "\">" + NAME_VALUE + "</a>,</b></p>", "<p>This is an example <i>HTML</i> email sent by Seam 3 and Seam Render.</p>", "Importance: high"});
    }

    @Test
    public void freemarkerHtmlWithTextMailTest() {
        fillInInputs();
        sendEmail(SEND_HTML_TEXT_EMAIL_WITH_FREEMARKER);
        checkDelivered(new String[]{"Content-Type: text/plain", "Content-Type: multipart/alternative", "Content-Type: multipart/mixed", "Content-Type: image/png; name=seamLogo.png", "Content-Disposition: inline", "Hello " + NAME_VALUE, "This is the alternative text body for mail readers that don't support html. This was sent with Seam 3 and FreeMarker.", "Importance: low"});
    }

    @Test
    public void seamHtmlWithTextMailTest() {
        fillInInputs();
        sendEmail(SEND_HTML_TEXT_EMAIL_WITH_SEAM);
        checkDelivered(new String[]{"Content-Type: text/plain", "Content-Type: multipart/alternative", "Content-Type: multipart/mixed", "Content-Type: image/png; name=seamLogo.png", "Content-Disposition: inline", "Hello " + NAME_VALUE, "This is the alternative text body for mail readers that don't support html. This was sent with Seam 3 and Seam Render.", "Importance: low"});
    }

    @Test
    public void velocityHtmlWithTextMailTest() {
        fillInInputs();
        sendEmail(SEND_HTML_TEXT_EMAIL_WITH_VELOCITY);
        checkDelivered(new String[]{"Content-Type: text/plain", "Content-Type: multipart/alternative", "Content-Type: multipart/mixed", "Content-Type: image/png; name=seamLogo.png", "Content-Disposition: inline", "Hello " + NAME_VALUE, "This is the alternative text body for mail readers that don't support html. This was sent with Seam 3 and Velocity.", "Importance: low"});
    }

    /**
     * Fills in html text inputs.
     */
    private void fillInInputs() {
        selenium.type(NAME, NAME_VALUE);
        selenium.type(ADDRESS, ADDRESS_VALUE);
    }

    /**
     * Sends an email by clicking on specified button.
     *
     * @param buttonToClick
     */
    private void sendEmail(XPathLocator buttonToClick) {
        waitForHttp(selenium).click(buttonToClick);
    }

    /**
     * Checks that the expected email was delivered.
     *
     * @param expectedMessageContents
     */
    private void checkDelivered(String[] expectedMessageContents) {
        assertFalse("Expected a message", wiser.getMessages().isEmpty());
        WiserMessage message = wiser.getMessages().get(0); // although "send plain text" example sends 3 mails (To:, CC:, Bcc:) Wiser cannot distinquish between them so we just check the first mail.
        assertEquals(ADDRESS_VALUE, message.getEnvelopeReceiver());
        assertTrue("Envelope sender (" + message.getEnvelopeSender() + ") doesn't match expected one (" + ENVELOPE_SENDER + ")", message.getEnvelopeSender().matches(ENVELOPE_SENDER));

        for (String expectedMessageContent : expectedMessageContents) {
            assertTrue("Didn't find expected text (" + expectedMessageContent + ") in the received email.", new String(message.getData()).contains(expectedMessageContent));
        }
    }

}
