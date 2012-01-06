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

import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Extends {@link MimeMessage} to allow for the setting of the Message-ID
 * 
 * @author cody.lerum
 */
public class RootMimeMessage extends MimeMessage {
    private String messageId;

    public RootMimeMessage(Session session) {
        super(session);
    }

    public RootMimeMessage(Session session, InputStream inputStream) throws MessagingException {
        super(session, inputStream);
    }

    @Override
    protected void updateMessageID() throws MessagingException {
        Header header = new Header("Message-ID", messageId);
        setHeader(header.getName(), header.getValue());
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
