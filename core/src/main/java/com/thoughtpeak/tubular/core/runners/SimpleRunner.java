package com.thoughtpeak.tubular.core.runners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;
/**
 * Simple runner for testing pipeline algorithms that runs
 * within a single thread, processed one by one
 * 
 * @author chrisbeesley
 *
 */
public class SimpleRunner implements CoreRunner{

	

	@Override
	public <T extends BaseWorkItem,U> void execute(Pipeline pipeline,
			WorkListDocumentCollector<T,U> worklist) {
		
		Pipeline used = pipeline.createNewCopy();
		
		while(!worklist.isComplete()){
			U eachItem = worklist.getNext();
			T workItem = worklist.loadDocument(eachItem);
			CommonAnalysisStructure bin = used.executePipeline(workItem.getDocumentText());
			
			worklist.workItemCompleted(bin, workItem);
			
		}
		try {
			worklist.close();
			worklist.collectionProcessCompleted();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
