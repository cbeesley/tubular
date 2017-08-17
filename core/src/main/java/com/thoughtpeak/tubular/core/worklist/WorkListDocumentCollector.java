package com.thoughtpeak.tubular.core.worklist;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

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
 * @param <T> - the type that will be the subject of analysis
 * @param <U> - The type that will be the input class type for processing through the worklist
 */
public interface WorkListDocumentCollector<T extends BaseWorkItem,U> extends Closeable{
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
	 * This method gets called when a single work item is processed through the pipeline.
	 * 
	 * If you need to add a result that needs to be processed by a custom runner such as a new
	 * annotation that needs to be computed within this method, add it to the CAS then retrieve
	 * the annotations in the runner. The default runners do not do anything with the CAS so this 
	 * functionality needs to be implemented using a custom runner of your design.
	 * 
	 * 
	 * @param cas - The resulting common analysis structure with the annotations found in the unit of analysis
	 * @param workItem - The work item that was used to get the annotations from
	 */
	public void workItemCompleted(CommonAnalysisStructure cas, T workItem);

	/**
	 * This method gets called when all items in the worklist is completed process is completed. Implementations
	 * should then write the results to a channel, then remove reference to reclaim
	 * memory.
	 */
	public void collectionProcessCompleted();
		
	/**
	 * Close any open resources
	 * @throws IOException
	 */
	public void close() throws IOException;
	
	/**
	 * Load reports with content. Mainly for batch process.
	 * Auto generated method comment
	 * 
	 * @param itemList List of work items
	 * @return List<T>
	 */
	public List<T> loadDocuments(List<T> itemList);
}
