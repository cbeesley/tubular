package com.thoughtpeak.tubular.core.collection;
/**
 * Any classes that need to perform any kind of processing on the resulting
 * annotations produced by the pipeline, extend this class. This uses a decorator
 * design to construct the starting with a class that initializes the process then
 * does a chain style execution.
 * 
 * @author chrisbeesley
 *
 */
public abstract class BaseAnnotationChannel {
	
	protected AnnotationCollectionChannel evaluator;
	
	protected BaseAnnotationChannel(AnnotationCollectionChannel evaluator){
		this.evaluator = evaluator;
	}
	
	public void evaluateChannel(){
		// no op
	}

}
