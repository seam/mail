package org.jboss.seam.mail.templating.velocity;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.apache.velocity.context.AbstractContext;
import org.jboss.logging.Logger;

public class SeamCDIVelocityContext extends AbstractContext
{
   @Inject
   private Logger log;
   
   @Inject 
   private BeanManager beanManager;
   
   @Inject
   public SeamCDIVelocityContext()
   {
      super();
   }

   @Override
   public boolean internalContainsKey(Object key)
   {
      Set<Bean<?>> beans;
      beans = beanManager.getBeans(key.toString());
      
      if(beans.size() > 0)
      {
         return true;
      }              
      else
      {
         return false;
      }      
   }

   @Override
   public Object internalGet(String key)
   {
      log.debug("Getting Object by given EL name: " + key.toString());

      Bean<?> bean = null;
      Set<Bean<?>> beans;
      beans = beanManager.getBeans(key);
      
      log.debug("BeanManager got " + beans.size() + " beans for key: " + key);
      
      if(beans.size() > 0)
      {
         bean = beanManager.resolve(beans);            
      }
      else
      {
         return null;
      }

      if (bean != null)
      {
         log.debug("Found Bean by EL key: " + key.toString());
         return beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean));
      }
      else
      {
         log.debug("Returned Bean was Null");
         return null;
      }
   }

   @Override
   @Deprecated
   public Object[] internalGetKeys()
   {
      return null;
   }

   @Override
   @Deprecated
   public Object internalPut(String key, Object value)
   {
      return null;
   }

   @Override
   @Deprecated
   public Object internalRemove(Object key)
   {
      return null;
   }

}
