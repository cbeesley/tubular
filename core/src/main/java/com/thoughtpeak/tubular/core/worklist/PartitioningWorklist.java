package com.thoughtpeak.tubular.core.worklist;

import java.io.IOException;
import java.util.List;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;

/**
 * This is a adapter class that allows the extension of this to create worklists where the caller
 * needs to manage a group of source id's outside of the worklist
 * 
 * @author chrisbeesley
 *
 * @param <T> The output type used when collecting documents
 * @param <U> The source id type used to generate the document collection to iterate on
 */
public abstract class PartitioningWorklist<T extends BaseWorkItem,U> implements WorkListDocumentCollector<T,U>{

	@Override
	public U getNext() {
		throw new IllegalArgumentException("This method is not used");
		
	}

	@Override
	public boolean isComplete() {
		throw new IllegalArgumentException("This method is not used");
	}

	@Override
	public abstract void workItemCompleted(CommonAnalysisStructure cas, T workItem);

	@Override
	public abstract void collectionProcessCompleted();

	@Override
	public abstract void close() throws IOException;

	@Override
	public abstract List<U> getSourceIds();

	@Override
	public abstract List<T> loadDocuments(List<U> itemList);

}
