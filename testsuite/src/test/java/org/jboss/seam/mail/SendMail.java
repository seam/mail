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

import java.net.MalformedURLException;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.mail.Session;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.attachments.URLAttachment;
import org.jboss.seam.mail.core.enumerations.ContentDisposition;
import org.jboss.seam.mail.core.enumerations.MessagePriority;
import org.jboss.seam.mail.templating.freemarker.FreeMarkerTemplate;
import org.jboss.seam.mail.templating.velocity.CDIVelocityContext;
import org.jboss.seam.mail.templating.velocity.VelocityTemplate;
import org.jboss.solder.resourceLoader.ResourceProvider;

/**
 * @author Cody Lerum
 */
@Model
public class SendMail {
    private String text = "Simple Message with text body";

    @Inject
    private Instance<MailMessage> mailMessage;

    @Inject
    private Instance<CDIVelocityContext> cDIVelocityContext;

    @Inject
    private ResourceProvider resourceProvider;

    @Inject
    private Instance<Session> session;

    @Inject
    private Person person;

    public void sendText() {
        mailMessage.get()
                .from("seam@test.test", "Seam Framework")
                .to(person.getEmail(), person.getName())
                .subject("Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
                .bodyText(text)
                .send();
    }

    public void sendHTMLFreeMarker() throws MalformedURLException {
        mailMessage.get()
                .from("seam@test.test", "Seam Framework")
                .to(person)
                .subject("HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
                .bodyHtml(new FreeMarkerTemplate(resourceProvider.loadResourceStream("template.html.freemarker")))
                .put("person", person)
                .put("version", "Seam 3")
                .importance(MessagePriority.HIGH)
                .addAttachment(new URLAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
                .send(session.get());
    }

    public void sendHTMLwithAlternativeFreeMarker() throws MalformedURLException {
        mailMessage.get()
                .from("seam@test.test", "Seam Framework")
                .to(person.getEmail(), person.getName())
                .subject("HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
                .put("person", person)
                .put("version", "Seam 3")
                .bodyHtmlTextAlt(
                        new FreeMarkerTemplate(resourceProvider.loadResourceStream("template.html.freemarker")),
                        new FreeMarkerTemplate(resourceProvider.loadResourceStream("template.text.freemarker")))
                .importance(MessagePriority.LOW)
                .deliveryReceipt("seam@jboss.org")
                .readReceipt("seam@jboss.org")
                .addAttachment("template.html.freemarker", "text/html", ContentDisposition.ATTACHMENT, resourceProvider.loadResourceStream("template.html.freemarker"))
                .addAttachment(new URLAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
                .send(session.get());
    }

    public void sendHTMLVelocity() throws MalformedURLException {
        mailMessage.get()
                .from("seam@test.test", "Seam Framework")
                .to(person)
                .subject("HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
                .bodyHtml(new VelocityTemplate(resourceProvider.loadResourceStream("template.html.velocity"), cDIVelocityContext.get()))
                .put("version", "Seam 3")
                .importance(MessagePriority.HIGH)
                .addAttachment(new URLAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
                .send(session.get());
    }

    public void sendHTMLwithAlternativeVelocity() throws MalformedURLException {
        mailMessage.get()
                .from("seam@test.test", "Seam Framework")
                .to(person.getEmail(), person.getName())
                .subject("HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
                .put("version", "Seam 3")
                .bodyHtmlTextAlt(
                        new VelocityTemplate(resourceProvider.loadResourceStream("template.html.velocity"), cDIVelocityContext.get()),
                        new VelocityTemplate(resourceProvider.loadResourceStream("template.text.velocity"), cDIVelocityContext.get()))
                .importance(MessagePriority.LOW)
                .deliveryReceipt("seam@jboss.org")
                .readReceipt("seam@jboss.org")
                .addAttachment("template.html.velocity", "text/html", ContentDisposition.ATTACHMENT, resourceProvider.loadResourceStream("template.html.velocity"))
                .addAttachment(new URLAttachment("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png", "seamLogo.png", ContentDisposition.INLINE))
                .send(session.get());
    }
}
