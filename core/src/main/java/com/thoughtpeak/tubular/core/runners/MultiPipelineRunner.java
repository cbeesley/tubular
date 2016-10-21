package com.thoughtpeak.tubular.core.runners;

import java.util.List;

import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;
/**
 * This allows the execution of multiple configurations of the pipeline. Some implementations
 * will execute these in parallel or one at a time
 * 
 * @author chrisbeesley
 *
 */
public interface MultiPipelineRunner {
	
	
	/**
	 * This method gives the runner an option to execute multiple pipelines that have different types of annotators
	 * upon which the results can then be joined together once each pipeline completes. 
	 * 
	 * 
	 * @param pipeline
	 * @param worklist
	 */
	 public <T extends BaseWorkItem> void executeMultiplePipelines(List<Pipeline> pipelines, WorkListDocumentCollector<T> worklist);



}
