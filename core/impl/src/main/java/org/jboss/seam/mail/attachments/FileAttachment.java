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

import java.io.File;
import java.io.IOException;

import javax.activation.FileDataSource;

import com.google.common.io.Files;
import org.jboss.seam.mail.core.AttachmentException;
import org.jboss.seam.mail.core.Header;
import org.jboss.seam.mail.core.enumerations.ContentDisposition;

/**
 * @author Cody Lerum
 */
public class FileAttachment extends BaseAttachment {
    public FileAttachment(ContentDisposition contentDisposition, File file) {
        super();

        FileDataSource fds = new FileDataSource(file);

        try {
            super.setFileName(fds.getName());
            super.setMimeType(fds.getContentType());
            super.setContentDisposition(contentDisposition);
            super.setBytes(Files.toByteArray(file));
        } catch (IOException e) {
            throw new AttachmentException("Wasn't able to create email attachment from File: " + file.getName(), e);
        }
    }

    public FileAttachment(ContentDisposition contentDisposition, File file, String contentClass) {
        this(contentDisposition, file);
        super.addHeader(new Header("Content-Class", contentClass));
    }
}
