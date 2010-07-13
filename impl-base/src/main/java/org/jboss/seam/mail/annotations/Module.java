package org.jboss.seam.mail.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * General Module Qualifier to prevent contamination of environment with 
 * {@code @Producer}s of common types.
 *  (e.g. {@code ConnectionFactory}, {@code InitialContext}, ...)
 * 
 * @author Cody Lerum
 */
@Qualifier
@Target( { FIELD, METHOD, TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface Module
{
}