package com.thoughtpeak.tubular.core.worklist;

import java.io.IOException;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;



public abstract class WorklistAdapter<T extends BaseWorkItem> implements WorkListDocumentCollector<T>{

	@Override
	public T getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public abstract void workItemCompleted(CommonAnalysisStructure cas, T workItem);
		

	@Override
	public void collectionProcessCompleted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
