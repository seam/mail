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

package org.jboss.seam.mail.attachments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.jboss.seam.mail.core.EmailAttachment;
import org.jboss.seam.mail.core.Header;
import org.jboss.seam.mail.core.enumerations.ContentDisposition;

/**
 * @author Cody Lerum
 */
public class BaseAttachment implements EmailAttachment {
    private String contentId;
    private String fileName;
    private String mimeType;
    private ContentDisposition contentDisposition;
    private Collection<Header> headers = new ArrayList<Header>();
    private byte[] bytes;

    public BaseAttachment(String fileName, String mimeType, ContentDisposition contentDisposition, byte[] bytes) {
        this();
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.contentDisposition = contentDisposition;
        this.bytes = bytes;
    }

    public BaseAttachment(String fileName, String mimeType, ContentDisposition contentDisposition, byte[] bytes,
            String contentClass) {
        this(fileName, mimeType, contentDisposition, bytes);
        this.addHeader(new Header("Content-Class", contentClass));
    }

    public BaseAttachment() {
        this.contentId = UUID.randomUUID().toString();
    }

    public String getContentId() {
        return contentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(ContentDisposition contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public Collection<Header> getHeaders() {
        return headers;
    }

    public void addHeader(Header header) {
        headers.add(header);
    }

    public void addHeaders(Collection<Header> headers) {
        headers.addAll(headers);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
