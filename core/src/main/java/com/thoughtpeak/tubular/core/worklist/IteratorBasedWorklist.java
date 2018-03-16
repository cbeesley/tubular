package com.thoughtpeak.tubular.core.worklist;

import java.io.IOException;
import java.util.List;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
/**
 * This worklist is used for when the loading of documents is done all at once or incrementally
 * 
 * @author chrisbeesley
 *
 * @param <T> - The type that is to be processed through the pipeline
 * @param <U> - A source type that can be used to pull in the documents using some form of id(s). This type can just be
 * the same as T if you dont need an id to get a document
 */
public abstract class IteratorBasedWorklist <T extends BaseWorkItem,U> implements WorkListDocumentCollector<T,U> {

	@Override
	public abstract U getNext();

	@Override
	public abstract boolean isComplete();

	@Override
	public abstract void workItemCompleted(CommonAnalysisStructure cas, T workItem);

	@Override
	public abstract void collectionProcessCompleted();

	/**
	 * Optional: This is a no-op unless you override it
	 */
	@Override
	public void close() throws IOException {
		
	}

	@Override
	public List<U> getSourceIds() {
		throw new IllegalArgumentException("This method is not used");
	}

	@Override
	public List<T> loadDocuments(List<U> itemList) {
		throw new IllegalArgumentException("This method is not used");
	}
	
	@Override
	public abstract T loadDocument(U item);

}
