package org.jboss.seam.mail.api;

import org.jboss.seam.mail.templating.TemplateMailMessage;

/**
 * Interface for creating email messages using the <a href="http://velocity.apache.org/">Velocity</a> templating engine.
 * 
 * @author Cody Lerum
 */
public interface VelocityMailMessage extends TemplateMailMessage<VelocityMailMessage>
{

}
