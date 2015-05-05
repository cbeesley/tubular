package com.thoughtpeak.tubular.core.worklist;

import java.io.Closeable;
import java.io.IOException;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;

/**
 * This is the base interface for implementing a document
 * collector to process within a workflow. Implementations can use this
 * when the document or item collection is known or as an ephemeral based worklist
 * where items are constantly being processed. In the later case, the implementation can
 * use the worklist as a buffering mechanism.
 * 
 * @author chrisbeesley
 *
 * @param <T>
 */
public interface WorkListDocumentCollector<T extends BaseWorkItem> extends Closeable{
	/**
	 * Gets the next document in the worklist
	 * @return The defined type which is 
	 */
	public T getNext();
	/**
	 * Lets the runner know that the document collection
	 * has reach an end state
	 * @return
	 */
	public boolean isComplete();
	/**
	 * This method gets called when all items in the worklist is completed process is completed. Implementations
	 * should then write the results to a channel, then remove reference to reclaim
	 * memory.
	 * 
	 * When used in a persitent pipeline (IE, the items are always flowing into the worklist, then this could be used to 
	 * return the pipeline to a object pool.
	 * 
	 * @param bin - The resulting annotations
	 * @param workItem - The work item that was used to get the annotations from
	 */
	public void collectionProcessCompleted(CommonAnalysisStructure bin, T workItem);
	/**
	 * Close any open resources
	 * @throws IOException
	 */
	public void close() throws IOException;
	
	

}
