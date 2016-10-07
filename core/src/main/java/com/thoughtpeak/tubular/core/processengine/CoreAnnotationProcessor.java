package com.thoughtpeak.tubular.core.processengine;

import java.io.Serializable;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
/**
 * The base interface for all annotation processors. All implementation classes
 * must use a no argument constructor to allow for factory creation of instances.
 * 
 * For startup use and passing in things like configuration values, use the 
 * Initialize annotation on a method
 * 
 * For each implementation, you must take care to consider any instance variable in descendant
 * classes. Resources will be instantiated per thread. The ConncurrentRunner for example will create N copies to N 
 * threads its configured to use.
 * 
 * @author chrisbeesley
 *
 */
public interface CoreAnnotationProcessor extends Serializable {
	
	public void process(CommonAnalysisStructure cas);

}
