package org.jboss.seam.mail.templating;

import java.io.InputStream;

public class MailTemplate 
{
	private String name;
	private InputStream inputStream;

	public MailTemplate(String name, InputStream inputStream) {
		this.name = name;
		this.inputStream = inputStream;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
