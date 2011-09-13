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

package org.jboss.seam.mail.example;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.mail.Session;

import org.jboss.seam.logging.Logger;
import org.jboss.seam.mail.core.MailConfig;
import org.jboss.seam.mail.util.MailUtility;

/**
 * @author Cody Lerum
 */
public class GmailSessionProducer {
    @Inject
    private Logger log;

    @Gmail
    @Produces
    public Session getMailSession() {
        log.debug("Producing Gmail Session");

        MailConfig mailConfig = new MailConfig();

        mailConfig.setServerHost("localhost");
        mailConfig.setServerPort(8978);
        mailConfig.setUsername("test");
        mailConfig.setPassword("test12!");
        mailConfig.setAuth(true);

        return MailUtility.buildMailSession(mailConfig);
    }
}
