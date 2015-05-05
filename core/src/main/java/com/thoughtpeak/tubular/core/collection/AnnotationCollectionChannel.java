package com.thoughtpeak.tubular.core.collection;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;

/**
 * Base interface for handling collection operations. When all annotations
 * have been gathered, the api user can specify a processor like a drools based rule engine
 * or create csv style results or
 * to run or a custom implementation of the user's design.
 * 
 * @author chrisbeesley
 *
 */
public interface AnnotationCollectionChannel {
	/**
	 * Trigger the evaluation on the bin after all annotations
	 * are collected
	 * 
	 * @param bin - The final bin with all annotations and indexes
	 */
	public void evaluateAnnotationCollection(CommonAnalysisStructure bin);

}
