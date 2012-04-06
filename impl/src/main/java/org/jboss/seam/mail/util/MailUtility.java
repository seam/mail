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

package org.jboss.seam.mail.util;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.seam.mail.core.BaseMailMessage;
import org.jboss.seam.mail.core.EmailContact;
import org.jboss.seam.mail.core.EmailMessage;
import org.jboss.seam.mail.core.InvalidAddressException;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.core.MailException;
import org.jboss.seam.mail.core.MailSessionAuthenticator;
import org.jboss.seam.mail.core.SendFailedException;
import org.jboss.seam.mail.core.enumerations.EmailMessageType;
import org.jboss.seam.mail.core.enumerations.RecipientType;

/**
 * @author Cody Lerum
 */
public class MailUtility {
    public static InternetAddress internetAddress(String address) throws InvalidAddressException {
        try {
            return new InternetAddress(address);
        } catch (AddressException e) {
            throw new InvalidAddressException("Must be in format of a@b.com or Name<a@b.com> but was: \"" + address + "\"", e);
        }
    }

    public static Collection<InternetAddress> internetAddress(String... addresses) throws InvalidAddressException {
        ArrayList<InternetAddress> result = new ArrayList<InternetAddress>();

        for (String address : addresses) {
            result.add(MailUtility.internetAddress(address));
        }
        return result;
    }

    public static InternetAddress internetAddress(String address, String name) throws InvalidAddressException {
        InternetAddress internetAddress;
        try {
            internetAddress = new InternetAddress(address);
            internetAddress.setPersonal(name);
            return internetAddress;
        } catch (AddressException e) {
            throw new InvalidAddressException(e);
        } catch (UnsupportedEncodingException e) {
            throw new InvalidAddressException(e);
        }
    }

    public static InternetAddress internetAddress(EmailContact emailContact) throws InvalidAddressException {
        if (Strings.isNullOrBlank(emailContact.getName())) {
            return MailUtility.internetAddress(emailContact.getAddress());
        } else {
            return MailUtility.internetAddress(emailContact.getAddress(), emailContact.getName());
        }
    }

    public static Collection<InternetAddress> internetAddress(Collection<? extends EmailContact> emailContacts)
            throws InvalidAddressException {
        Set<InternetAddress> internetAddresses = new HashSet<InternetAddress>();

        for (EmailContact ec : emailContacts) {
            internetAddresses.add(MailUtility.internetAddress(ec));
        }

        return internetAddresses;
    }

    public static InternetAddress[] getInternetAddressses(InternetAddress emaiAddress) {
        InternetAddress[] internetAddresses = { emaiAddress };

        return internetAddresses;
    }

    public static InternetAddress[] getInternetAddressses(Collection<InternetAddress> recipients) {
        InternetAddress[] result = new InternetAddress[recipients.size()];
        recipients.toArray(result);
        return result;
    }

    public static String getHostName() {
        try {
            java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
            return localMachine.getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    public static Session createSession(MailConfig mailConfig) {

        if (!Strings.isNullOrBlank(mailConfig.getJndiSessionName())) {
            try {
                return InitialContext.doLookup(mailConfig.getJndiSessionName());
            } catch (NamingException e) {
                throw new MailException("Unable to lookup JNDI JavaMail Session", e);
            }
        }

        Session session;

        Properties props = new Properties();

        if (mailConfig.isValid()) {
            props.setProperty("mail.smtp.host", mailConfig.getServerHost());
            props.setProperty("mail.smtp.port", mailConfig.getServerPort().toString());
            props.setProperty("mail.smtp.starttls.enable", mailConfig.getEnableSsl().toString());
            props.setProperty("mail.smtp.starttls.required", mailConfig.getRequireTls().toString());
            props.setProperty("mail.smtp.ssl.enable", mailConfig.getEnableSsl().toString());
            props.setProperty("mail.smtp.auth", mailConfig.getAuth().toString());
        } else {
            throw new MailException("Server Host and Server  Port must be set in MailConfig");
        }

        if (!Strings.isNullOrBlank(mailConfig.getDomainName())) {
            props.put("mail.seam.domainName", mailConfig.getDomainName());
        }

        if (mailConfig.getUsername() != null && mailConfig.getUsername().length() != 0 && mailConfig.getPassword() != null
                && mailConfig.getPassword().length() != 0) {
            MailSessionAuthenticator authenticator = new MailSessionAuthenticator(mailConfig.getUsername(),
                    mailConfig.getPassword());

            session = Session.getInstance(props, authenticator);
        } else {
            session = Session.getInstance(props, null);
        }

        return session;
    }

    public static String headerStripper(String header) {
        if (!Strings.isNullOrBlank(header)) {
            String s = header.trim();

            if (s.matches("^<.*>$")) {
                return header.substring(1, header.length() - 1);
            } else {
                return header;
            }
        } else {
            return header;
        }
    }

    public static MimeMessage createMimeMessage(EmailMessage e, Session session) {
        BaseMailMessage b = new BaseMailMessage(session, e.getCharset(), e.getRootContentType());

        if (!Strings.isNullOrBlank(e.getMessageId())) {
            b.setMessageID(e.getMessageId());
        }

        b.setFrom(e.getFromAddresses());
        b.addRecipients(RecipientType.TO, e.getToAddresses());
        b.addRecipients(RecipientType.CC, e.getCcAddresses());
        b.addRecipients(RecipientType.BCC, e.getBccAddresses());
        b.setReplyTo(e.getReplyToAddresses());
        b.addDeliveryRecieptAddresses(e.getDeliveryReceiptAddresses());
        b.addReadRecieptAddresses(e.getReadReceiptAddresses());
        b.setImportance(e.getImportance());
        b.addHeaders(e.getHeaders());

        if (e.getSubject() != null) {
            b.setSubject(e.getSubject());
        }

        if (e.getType() == EmailMessageType.STANDARD) {

            if (e.getHtmlBody() != null && e.getTextBody() != null) {
                b.setHTMLTextAlt(e.getHtmlBody(), e.getTextBody());
            } else if (e.getTextBody() != null) {
                b.setText(e.getTextBody());
            } else if (e.getHtmlBody() != null) {
                b.setHTML(e.getHtmlBody());
            }

            b.addAttachments(e.getAttachments());
        } else if (e.getType() == EmailMessageType.INVITE_ICAL) {
            b.setHTMLNotRelated(e.getHtmlBody());
            b.addAttachments(e.getAttachments());
        } else {
            throw new SendFailedException("Unsupported Message Type: " + e.getType());
        }

        MimeMessage msg = b.getFinalizedMessage();

        return msg;
    }

    public static void send(EmailMessage e, Session session) throws SendFailedException {
        MimeMessage msg = MailUtility.createMimeMessage(e, session);
        try {
            Transport.send(msg);
        } catch (MessagingException e1) {
            throw new SendFailedException("Send Failed", e1);
        }

        try {
            e.setMessageId(null);
            e.setLastMessageId(MailUtility.headerStripper(msg.getMessageID()));
        } catch (MessagingException e1) {
            throw new SendFailedException("Unable to read Message-ID from sent message");
        }
    }
}
