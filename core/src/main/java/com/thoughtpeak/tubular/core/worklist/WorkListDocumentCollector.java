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
	public U getNext();
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
	 * This method allows access to the sourceIds(U) so they can parallelize the entire collection directly instead of calling the getNext()
	 * in the worklist. This is useful in cases where you want something like Spark to handle the retrieval of the source
	 * text for example, from a http based service during the paralellization process as opposed to materializing 
	 * the entire collection at once if you have a large amount of data to process.
	 * 
	 * @return A list of U source id types to create the initial set. If this is null or empty, then the runner will attempt to call 
	 * the getNext() in the worklist.
	 */
	public List<U> getSourceIds();
	
	/**
	 * Load reports with content. Mainly for batch process.
	 * Auto generated method comment
	 * 
	 * @param itemList List of work items of type U that is used as a source id
	 * @return List<T>
	 */
	public List<T> loadDocuments(List<U> itemList);
	/**
	 * Loads a single document
	 * 
	 * @param item - A source object of some kind of type U
	 * 
	 * @return - A new object that extends BaseWorkItem that will be used in the pipelines
	 */
	public T loadDocument(U item);
    
    /**
     * Write the results after all has been processed. We discovered that this method is needed for
     * runners that need write the results all together after other processes have been done (like
     * ngram. It cannot write result one by one when one item is processed).
     * 
     * @param <E> type E in List
     * @param results List
     */
    public <E> void writeResults(List<E> results);
}
