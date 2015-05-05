package com.thoughtpeak.tubular.core;

import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;

/**
 * Default workflow execution
 * 
 * @author chrisbeesley
 *
 */
public class Tubular {
	
	public Tubular(ExecutionPlan workflow){
		
		workflow.getConfiguration();
		Pipeline pipeline = workflow.getPipeline();
		WorkListDocumentCollector<?> worklist = workflow.getWorkList();
		workflow.getRunner().execute(pipeline, worklist);
		
	}
	
	public static void main(String ... args){
		
	}

}
