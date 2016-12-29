package com.thoughtpeak.tubular.core;

import com.thoughtpeak.tubular.core.collection.BaseAnnotationChannel;
import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.runners.CoreRunner;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;

/**
 * A execution plan is the configuration of pipelines, workflow, runner, and collection
 * processes which define the overall job of taking a group of documents or objects
 * and producing the desired output. 
 * 
 * @author chrisbeesley
 *
 */
public interface ExecutionPlan {
	
	public void getConfiguration();
	
	public WorkListDocumentCollector<?,?> getWorkList();
	
	public Pipeline getPipeline();
	
	public CoreRunner getRunner();
	
	public BaseAnnotationChannel getCollectionChain();

}
