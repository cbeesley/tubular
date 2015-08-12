package com.thoughtpeak.tubular.core.processengine;

import java.io.Serializable;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
/**
 * The base interface for all annotation processors. All implementation classes
 * must use a no argument constructor
 * 
 * @author chrisbeesley
 *
 */
public interface CoreAnnotationProcessor extends Serializable {
	
	public void process(CommonAnalysisStructure cas);

}
