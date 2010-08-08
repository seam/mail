package org.jboss.seam.mail.core;

import javax.inject.Inject;
import javax.mail.Session;

import org.jboss.seam.mail.annotations.Module;
import org.jboss.seam.mail.api.StandardMailMessage;

public class StandardMailMessageImpl extends BaseMailMessage<StandardMailMessage> implements StandardMailMessage
{
   @Inject
   public StandardMailMessageImpl(@Module Session session)
   {
      super(session);
   }

   /**
    * {@inheritDoc}
    * @see org.jboss.seam.mail.core.BaseMailMessage#getRealClass()
    */
   @Override
   protected Class<StandardMailMessage> getRealClass()
   {
      return StandardMailMessage.class;
   }
}
