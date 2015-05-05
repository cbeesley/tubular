package com.thoughtpeak.tubular.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A configuration parameter allows a configuration based file like a property file or
 * json to be used to set parameters that the AnalysisComponent classes use for initialization
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigParameter {

	/**
	 * The parameter name to look up to get the
	 * value need to set on the field that this annotation is
	 * defined
	 * 
	 * @return
	 */
	String paramterName() default "none";
}
