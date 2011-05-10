package org.jboss.seam.mail.templating;

import java.util.Map;

public interface TemplateProvider {
    public String merge(Map<String, Object> context);
}
