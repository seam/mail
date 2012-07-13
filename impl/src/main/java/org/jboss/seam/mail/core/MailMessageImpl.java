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

package org.jboss.seam.mail.core;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.attachments.BaseAttachment;
import org.jboss.seam.mail.attachments.FileAttachment;
import org.jboss.seam.mail.attachments.InputStreamAttachment;
import org.jboss.seam.mail.core.enumerations.ContentDisposition;
import org.jboss.seam.mail.core.enumerations.ContentType;
import org.jboss.seam.mail.core.enumerations.EmailMessageType;
import org.jboss.seam.mail.core.enumerations.MessagePriority;
import org.jboss.seam.mail.templating.MailContext;
import org.jboss.seam.mail.templating.TemplateProvider;
import org.jboss.seam.mail.util.EmailAttachmentUtil;
import org.jboss.seam.mail.util.MailUtility;
import org.jboss.solder.core.ExtensionManaged;
import org.jboss.solder.logging.Logger;

/**
 * @author Cody Lerum
 */
public class MailMessageImpl implements MailMessage {

    private EmailMessage emailMessage;

    private TemplateProvider subjectTemplate;
    private TemplateProvider textTemplate;
    private TemplateProvider htmlTemplate;
    private Map<String, Object> templateContext = new HashMap<String, Object>();
    private boolean templatesMerged;
    private MailTransporter mailTransporter;

    @Inject
    @ExtensionManaged
    private Instance<Session> session;

    @Inject
    private Logger log;

    public MailMessageImpl() {
        emailMessage = new EmailMessage();
    }

    // Begin Addressing

    public MailMessage from(String... address) {
        emailMessage.addFromAddresses(MailUtility.internetAddress(address));
        return this;
    }

    public MailMessage from(InternetAddress emailAddress) {
        emailMessage.addFromAddress(emailAddress);
        return this;
    }

    public MailMessage from(EmailContact emailContact) {
        if (emailContact != null) {
            emailMessage.addFromAddress(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    public MailMessage from(Collection<? extends EmailContact> emailContacts) {
        emailMessage.addFromAddresses(MailUtility.internetAddress(emailContacts));
        return this;
    }

    public MailMessage replyTo(String... address) {
        emailMessage.addReplyToAddresses(MailUtility.internetAddress(address));
        return this;
    }

    public MailMessage replyTo(InternetAddress emailAddress) {
        emailMessage.addReplyToAddress(emailAddress);
        return this;
    }

    public MailMessage replyTo(EmailContact emailContact) {
        if (emailContact != null) {
            emailMessage.addReplyToAddress(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    public MailMessage replyTo(Collection<? extends EmailContact> emailContacts) {
        emailMessage.addReplyToAddresses(MailUtility.internetAddress(emailContacts));
        return this;
    }

    public MailMessage addHeader(String name, String value) {
        emailMessage.addHeader(new Header(name, value));
        return this;
    }

    public MailMessage to(String... address) {
        emailMessage.addToAddresses(MailUtility.internetAddress(address));
        return this;
    }

    public MailMessage to(InternetAddress emailAddress) {
        emailMessage.addToAddress(emailAddress);
        return this;
    }

    public MailMessage to(EmailContact emailContact) {
        if (emailContact != null) {
            emailMessage.addToAddress(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    public MailMessage to(Collection<? extends EmailContact> emailContacts) {
        emailMessage.addToAddresses(MailUtility.internetAddress(emailContacts));
        return this;
    }

    public MailMessage cc(String... address) {
        emailMessage.addCcAddresses(MailUtility.internetAddress(address));
        return this;
    }

    public MailMessage cc(InternetAddress emailAddress) {
        emailMessage.addCcAddress(emailAddress);
        return this;
    }

    public MailMessage cc(EmailContact emailContact) {
        if (emailContact != null) {
            emailMessage.addCcAddress(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    public MailMessage cc(Collection<? extends EmailContact> emailContacts) {
        emailMessage.addCcAddresses(MailUtility.internetAddress(emailContacts));
        return this;
    }

    public MailMessage bcc(String... address) {
        emailMessage.addBccAddresses(MailUtility.internetAddress(address));
        return this;
    }

    public MailMessage bcc(InternetAddress emailAddress) {
        emailMessage.addBccAddress(emailAddress);
        return this;
    }

    public MailMessage bcc(EmailContact emailContact) {
        if (emailContact != null) {
            emailMessage.addBccAddress(MailUtility.internetAddress(emailContact));
        }
        return this;
    }

    public MailMessage bcc(Collection<? extends EmailContact> emailContacts) {
        emailMessage.addBccAddresses(MailUtility.internetAddress(emailContacts));
        return this;
    }

    // End Addressing

    public MailMessage subject(String value) {
        emailMessage.setSubject(value);
        return this;
    }

    public MailMessage deliveryReceipt(String address) {
        emailMessage.addDeliveryReceiptAddress(MailUtility.internetAddress(address));
        return this;
    }

    public MailMessage readReceipt(String address) {
        emailMessage.addReadReceiptAddress(MailUtility.internetAddress(address));
        return this;
    }

    public MailMessage importance(MessagePriority messagePriority) {
        emailMessage.setImportance(messagePriority);
        return this;
    }

    public MailMessage messageId(String messageId) {
        emailMessage.setMessageId(messageId);
        return this;
    }

    public MailMessage bodyText(String text) {
        emailMessage.setTextBody(text);
        return this;
    }

    public MailMessage bodyHtml(String html) {
        emailMessage.setHtmlBody(html);
        return this;
    }

    public MailMessage bodyHtmlTextAlt(String html, String text) {
        emailMessage.setTextBody(text);
        emailMessage.setHtmlBody(html);
        return this;
    }

    // Begin Attachments

    public MailMessage addAttachment(EmailAttachment attachment) {
        emailMessage.addAttachment(attachment);
        return this;
    }

    public MailMessage addAttachments(Collection<? extends EmailAttachment> attachments) {
        emailMessage.addAttachments(attachments);
        return this;
    }

    public MailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDispostion, byte[] bytes) {
        addAttachment(new BaseAttachment(fileName, mimeType, contentDispostion, bytes));
        return this;
    }

    public MailMessage addAttachment(String fileName, String mimeType, ContentDisposition contentDispostion,
            InputStream inputStream) {
        addAttachment(new InputStreamAttachment(fileName, mimeType, contentDispostion, inputStream));
        return this;
    }

    public MailMessage addAttachment(ContentDisposition contentDispostion, File file) {
        addAttachment(new FileAttachment(contentDispostion, file));
        return this;
    }

    // End Attachments

    // Begin Calendar

    public MailMessage iCal(String html, byte[] bytes) {
        emailMessage.setType(EmailMessageType.INVITE_ICAL);
        emailMessage.setHtmlBody(html);
        emailMessage.addAttachment(new BaseAttachment(null, "text/calendar;method=CANCEL", ContentDisposition.INLINE, bytes,
                "urn:content-classes:calendarmessage"));
        return this;
    }

    // End Calendar

    public MailMessage subject(TemplateProvider subject) {
        subjectTemplate = subject;
        return this;
    }

    public MailMessage bodyText(TemplateProvider textBody) {
        textTemplate = textBody;
        return this;
    }

    public MailMessage bodyHtml(TemplateProvider htmlBody) {
        htmlTemplate = htmlBody;
        return this;
    }

    public MailMessage bodyHtmlTextAlt(TemplateProvider htmlBody, TemplateProvider textBody) {
        bodyHtml(htmlBody);
        bodyText(textBody);
        return this;
    }

    public MailMessage charset(String charset) {
        emailMessage.setCharset(charset);
        return this;
    }

    public MailMessage contentType(ContentType contentType) {
        emailMessage.setRootContentType(contentType);
        return this;
    }

    public MailMessage put(String key, Object value) {
        templateContext.put(key, value);
        return this;
    }

    public MailMessage put(Map<String, Object> values) {
        templateContext.putAll(values);
        return this;
    }

    public EmailMessage getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    public void setMailTransporter(MailTransporter mailTransporter) {
        this.mailTransporter = mailTransporter;
    }

    public EmailMessage mergeTemplates() {

        log.debug("Merging templates");

        put("mailContext", new MailContext(EmailAttachmentUtil.getEmailAttachmentMap(emailMessage.getAttachments())));

        if (subjectTemplate != null) {
            emailMessage.setSubject(subjectTemplate.merge(templateContext));
        }

        if (textTemplate != null) {
            emailMessage.setTextBody(textTemplate.merge(templateContext));
        }

        if (htmlTemplate != null) {
            emailMessage.setHtmlBody(htmlTemplate.merge(templateContext));
        }

        templatesMerged = true;

        return emailMessage;
    }

    public EmailMessage send(MailTransporter mailTransporter) throws SendFailedException {

        log.debug("Beginning send");

        if (!templatesMerged) {
            mergeTemplates();
        }

        try {
            mailTransporter.send(emailMessage);
        } catch (Exception e) {
            throw new SendFailedException("Send Failed", e);
        }

        return emailMessage;
    }

    public EmailMessage send(Session session) throws SendFailedException {
        return send(new MailTransporterImpl(session));
    }

    public EmailMessage send(MailConfig mailConfig) {
        return send(MailUtility.createSession(mailConfig));
    }

    public EmailMessage send() throws SendFailedException {

        if (mailTransporter != null) {
            return send(mailTransporter);
        } else {
            return this.send(session.get());
        }

    }
}
