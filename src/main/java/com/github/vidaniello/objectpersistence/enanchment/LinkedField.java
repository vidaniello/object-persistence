package com.github.vidaniello.objectpersistence.enanchment;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used as an annotation on methods, if specified, it tells the encher which field to point to to build the objects needed by the persistence engine.
 * @author Vincenzo D'Aniello (vidaniello@gmail.com) github.com/vidaniello
 *
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface LinkedField {
	
	
	
	/**
	 * Value of property.
	 * @return
	 */
	String value();
}
