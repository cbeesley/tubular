package com.thoughtpeak.tubular.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Marks the class as an analysis component of the pipeline configuration
 * Optionally, can specify if there are required components that need to be used beforehand
 * 
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnalysisComponent {
	
	String name();
	/**
	 * Tells the execution pipeline to order processing of this AC to be after
	 * any of the defined annotators
	 * @return A list of defined annotators
	 */
	String[] dependsOnAnyOf() default "none";
	/**
	 * Tells the execution pipeline to make sure that this component will be executed
	 * after all of the defined analysis components have been run
	 * 
	 * @return A list of defined annotators
	 */
	String[] dependsOnAllOf() default "none";

}
