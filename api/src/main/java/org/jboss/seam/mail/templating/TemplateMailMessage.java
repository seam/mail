package org.jboss.seam.mail.templating;

import java.io.File;

import org.jboss.seam.mail.core.MailMessage;
import org.jboss.seam.mail.exception.SeamMailException;

/**
 * Interface for creating email messages using a templating engine.
 * 
 * @author Cody Lerum
 */
public interface TemplateMailMessage<T extends TemplateMailMessage<T>> extends MailMessage<T>
{

   /**
    * Sets the body of the message to the plain text output of the given
    * template
    * 
    * @param textTemplateFile File of the template
    *           classpath
    * @throws SeamMailException
    */
   public T setTemplateText(File textTemplateFile) throws SeamMailException;

   /**
    * Sets the body of the message to the HTML output of the given template
    * 
    * @param htmlTemplateFile File of the template
    *           
    * @throws SeamMailException
    */
   public T setTemplateHTML(File htmlTemplateFile) throws SeamMailException;

   /**
    * Sets the body of the message to a HTML body with a plain text alternative
    * output of the given templates
    * 
    * @param htmlTemplateFile File of the template
    * @param textTemplateFile File of the template
    * @throws SeamMailException
    */
   public T setTemplateHTMLTextAlt(File htmlTemplateFile, File textTemplateFile) throws SeamMailException;
   
   /**
    * Sets the body of the message to the plain text output of the given
    * template
    * 
    * @param templateFileName Filename of the template to be found in the
    *           classpath
    * @throws SeamMailException
    */
   public T setTemplateText(String templateFileName) throws SeamMailException;

   /**
    * Sets the body of the message to the HTML output of the given template
    * 
    * @param templateFileName Filename of the template to be found in the
    *           classpath
    * @throws SeamMailException
    */
   public T setTemplateHTML(String templateFileName) throws SeamMailException;

   /**
    * Sets the body of the message to a HTML body with a plain text alternative
    * output of the given templates
    * 
    * @param htmlTemplateFileName Filename of the template to be found in the classpath
    * @param textTemplateFileName Filename of the template to be found in the classpath
    * @throws SeamMailException
    */
   public T setTemplateHTMLTextAlt(String htmlTemplateFileName, String textTemplateFileName) throws SeamMailException;

   /**
    * Places a variable in the templating engines context
    * 
    * @param name Reference name of the object
    * @param value the Object being placed in the context
    */
   public T put(String name, Object value);

}
