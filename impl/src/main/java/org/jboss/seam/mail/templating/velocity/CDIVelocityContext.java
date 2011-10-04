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

package org.jboss.seam.mail.templating.velocity;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.velocity.context.AbstractContext;
import org.jboss.solder.logging.Logger;

/**
 * @author Cody Lerum
 */
public class CDIVelocityContext extends AbstractContext {
    @Inject
    private Logger log;

    @Inject
    private BeanManager beanManager;

    public CDIVelocityContext() {
        super();
    }

    @Override
    public boolean internalContainsKey(Object key) {
        Set<Bean<?>> beans;
        beans = beanManager.getBeans(key.toString());

        if (beans.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object internalGet(String key) {
        log.debug("Getting Object by given EL name: " + key.toString());

        Bean<?> bean = null;
        Set<Bean<?>> beans;
        beans = beanManager.getBeans(key);

        log.debug("BeanManager got " + beans.size() + " beans for key: " + key);

        if (beans.size() > 0) {
            bean = beanManager.resolve(beans);
        } else {
            return null;
        }

        if (bean != null) {
            log.debug("Found Bean by EL key: " + key.toString());
            return beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean));
        } else {
            log.debug("Returned Bean was Null");
            return null;
        }
    }

    @Override
    @Deprecated
    public Object[] internalGetKeys() {
        return null;
    }

    @Override
    @Deprecated
    public Object internalPut(String key, Object value) {
        return null;
    }

    @Override
    @Deprecated
    public Object internalRemove(Object key) {
        return null;
    }

}
