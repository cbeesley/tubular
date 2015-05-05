package com.thoughtpeak.tubular.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
/**
 * This annotation marks the field of which its value will be used
 * for statistics such as word counts. The type annotated must be a String.
 *  Currently does not support superclass annotation.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureTarget {
	
	
}
