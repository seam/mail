package org.jboss.seam.mail.core;

import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.solder.resourceLoader.ResourceProvider;

import com.google.common.io.ByteStreams;

public class ClassPathEmailAttachment extends BaseEmailAttachment
{
   public ClassPathEmailAttachment(String fileName, String mimeType, ContentDisposition contentDisposition)
   {
      super();     

      try
      {
         super.setFileName(fileName);
         super.setMimeType(mimeType);
         super.setContentDisposition(contentDisposition);
         super.setBytes(ByteStreams.toByteArray(inputStreamFromClassPath(fileName)));
      }
      catch (IOException e)
      {
         throw new AttachmentException("Wasn't able to create email attachment from Class Path file: " + fileName, e);
      }
   }

   public ClassPathEmailAttachment(String fileName, String mimeType, ContentDisposition contentDisposition, String contentClass)
   {
      this(fileName, mimeType, contentDisposition);
      super.addHeader(new Header("Content-Class", contentClass));
   }

   private InputStream inputStreamFromClassPath(String fileName)
   {
      BeanManager bm = getBeanManager();
      Bean<?> bean = bm.resolve(bm.getBeans(ResourceProvider.class));
      ResourceProvider resourceProvider = (ResourceProvider) bm.getReference(bean, bean.getBeanClass(), bm.createCreationalContext(bean));
      return resourceProvider.loadResourceStream(fileName);
   }

}
