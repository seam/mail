/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.mail;

import java.util.ArrayList;
import java.util.Collection;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.BasicEmailContact;
import org.jboss.seam.mail.core.EmailContact;
import org.jboss.seam.mail.core.InvalidAddressException;
import org.jboss.seam.mail.core.MailMessageImpl;
import org.junit.Test;

/**
 * @author Cody Lerum
 */
public class InternetAddressTest {
    @Test
    public void validAddresses() {
        MailMessage m = new MailMessageImpl();

        BasicEmailContact seam = new BasicEmailContact("seam@domain.test");
        BasicEmailContact seamey = new BasicEmailContact("seamey@domain.test");

        Collection<EmailContact> addresses = new ArrayList<EmailContact>();
        addresses.add(seamey);

        m.from("seam@domain.test", "Seam Seamerson<seam@domain.test>");
        m.from("Seam Seamerson<seam@domain.test>");
        m.from("seam@domain.test");
        m.from(seam);
        m.from(addresses);

        m.to("seam@domain.test", "Seam Seamerson<seam@domain.test>");
        m.to("Seam Seamerson<seam@domain.test>");
        m.to("seam@domain.test");
        m.to(seam);
        m.to(addresses);

        m.cc("seam@domain.test", "Seam Seamerson<seam@domain.test>");
        m.cc("Seam Seamerson<seam@domain.test>");
        m.cc("seam@domain.test");
        m.cc(seam);
        m.cc(addresses);

        m.bcc("seam@domain.test", "Seam Seamerson<seam@domain.test>");
        m.bcc("Seam Seamerson<seam@domain.test>");
        m.bcc("seam@domain.test");
        m.bcc(seam);
        m.bcc(addresses);

        m.replyTo("seam@domain.test", "Seam Seamerson<seam@domain.test>");
        m.replyTo("Seam Seamerson<seam@domain.test>");
        m.replyTo("seam@domain.test");
        m.replyTo(seam);
        m.replyTo(addresses);
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidFromSimpleAddresses() {
        MailMessage m = new MailMessageImpl();

        m.from("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidFromFullAddresses() {
        MailMessage m = new MailMessageImpl();

        m.from("foo @bar.com", "Woo");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidToSimpleAddresses() {
        MailMessage m = new MailMessageImpl();

        m.to("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidToFullAddresses() {
        MailMessage m = new MailMessageImpl();

        m.to("foo @bar.com", "Woo");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidCcSimpleAddresses() {
        MailMessage m = new MailMessageImpl();

        m.cc("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidCcFullAddresses() {
        MailMessage m = new MailMessageImpl();

        m.cc("foo @bar.com", "Woo");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidBccSimpleAddresses() {
        MailMessage m = new MailMessageImpl();

        m.bcc("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidbccFullAddresses() {
        MailMessage m = new MailMessageImpl();

        m.bcc("foo @bar.com", "Woo");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidReplyToSimpleAddresses() {
        MailMessage m = new MailMessageImpl();

        m.replyTo("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidReplyToFullAddresses() {
        MailMessage m = new MailMessageImpl();

        m.replyTo("foo @bar.com", "Woo");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidDeliveryReceipt() {
        MailMessage m = new MailMessageImpl();

        m.deliveryReceipt("woo foo @bar.com");
    }

    @Test(expected = InvalidAddressException.class)
    public void invalidReadReceipt() {
        MailMessage m = new MailMessageImpl();

        m.readReceipt("woo foo @bar.com");
    }
}
