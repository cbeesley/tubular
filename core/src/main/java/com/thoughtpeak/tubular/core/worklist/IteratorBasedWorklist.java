package com.thoughtpeak.tubular.core.worklist;

import java.io.IOException;
import java.util.List;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;

public abstract class IteratorBasedWorklist <T extends BaseWorkItem,U> implements WorkListDocumentCollector<T,U> {

	@Override
	public abstract U getNext();

	@Override
	public abstract boolean isComplete();

	@Override
	public abstract void workItemCompleted(CommonAnalysisStructure cas, T workItem);

	@Override
	public abstract void collectionProcessCompleted();

	@Override
	public abstract void close() throws IOException;

	@Override
	public List<U> getSourceIds() {
		throw new IllegalArgumentException("This method is not used");
	}

	@Override
	public List<T> loadDocuments(List<U> itemList) {
		throw new IllegalArgumentException("This method is not used");
	}

}
