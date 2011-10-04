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

import javax.enterprise.context.ApplicationScoped;

import com.google.common.base.Strings;

/**
 * Bean which holds Mail Session configuration options. Can be configured via
 * Seam XML
 *
 * @author Cody Lerum
 */
@ApplicationScoped
public class MailConfig {
    private String serverHost = "localhost";
    private int serverPort = 25;
    private String domainName;
    private String username;
    private String password;
    private boolean enableTls = false;
    private boolean requireTls = false;
    private boolean enableSsl = false;
    private boolean auth = false;

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnableTls() {
        return enableTls;
    }

    public void setEnableTls(boolean enableTls) {
        this.enableTls = enableTls;
    }

    public boolean isRequireTls() {
        return requireTls;
    }

    public void setRequireTls(boolean requireTls) {
        this.requireTls = requireTls;
    }

    public boolean isEnableSsl() {
        return enableSsl;
    }

    public void setEnableSsl(boolean enableSsl) {
        this.enableSsl = enableSsl;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isValid() {
        if (Strings.isNullOrEmpty(serverHost.trim())) {
            return false;
        }

        if (serverPort == 0) {
            return false;
        }

        return true;
    }
}
