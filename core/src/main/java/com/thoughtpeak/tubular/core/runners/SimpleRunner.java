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
	public <T extends BaseWorkItem> void execute(Pipeline pipeline,
			WorkListDocumentCollector<T> worklist) {
		
		Pipeline used = pipeline.createNewCopy();
		
		while(!worklist.isComplete()){
			T eachItem = worklist.getNext();
			CommonAnalysisStructure bin = used.executePipeline(eachItem.getDocumentText());
			
			worklist.collectionProcessCompleted(bin, eachItem);
			
		}
		try {
			worklist.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
