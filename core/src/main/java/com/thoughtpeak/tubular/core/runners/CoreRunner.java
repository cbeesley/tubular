package com.thoughtpeak.tubular.core.runners;

import java.util.List;

import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;

/**
 * The base interface for creating runners which handle the processing
 * of worklists and pipelines. When creating custom runners, allocate each Pipeline
 * instance to a thread. Each instance of the Pipeline and subsequent AnnotationProcessers could
 * have mutable instance variables which could cause unexpected behaviors.
 * 
 * @author chrisbeesley
 * @param <AnnotationBin>
 *
 */
public interface CoreRunner {
	
	public <T extends BaseWorkItem> void execute(Pipeline pipeline, WorkListDocumentCollector<T> worklist);
	/**
	 * This method gives the runner an option to execute multiple pipelines that have different types of annotators
	 * upon which the results can then be joined together once each pipeline completes. 
	 * Each runner might choose not to implement this and may call the execute method
	 * 
	 * @param pipeline
	 * @param worklist
	 */
	//public <T extends BaseWorkItem> void executeMultiplePipelines(List<Pipeline> pipelines, WorkListDocumentCollector<T> worklist);

}
