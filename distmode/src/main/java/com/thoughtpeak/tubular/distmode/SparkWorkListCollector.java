package com.thoughtpeak.tubular.distmode;

import java.util.List;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;
import com.thoughtpeak.tubular.distmode.types.MapperResultType;
/**
 * Custom worklist that provides extra functions for operating over a spark cluster
 * 
 * @author chrisbeesley
 *
 * @param <T> - The work item class that is extended from the BaseWorkItem that is used as the source to work on 
 * through the pipeline.
 * 
 * If your collection in the worklist is larger than memory can hold, then consider writing out the worklist to a text file
 * format on a cluster such as hdfs, then use the text loader as a data source which uses block size partitions.
 * 
 * You must also ensure that the classes you use that are defined in instance variables implement serializable or marked as
 * transient
 */
public interface SparkWorkListCollector<T extends BaseWorkItem> extends WorkListDocumentCollector<T>{
	/**
	 * Called by each pipeline run iteration, by the spark runner to retrieve the class annotations that you want to process
	 * in additional reduce/pair operations. 
	 * 
	 * When using, put your result processing logic in this method on the Worklist instead of workItemCompleted method. It
	 * is also advisable to minimize any computations and loops here since this method can be a potential bottleneck. If that is the
	 * case, then create a custom SparkRunner to divide the tasks into dedicated transforms and actions.
	 * 
	 * @param cas - The resulting annotations from a single pipeline run
	 * @param workItem - The work item class being analyzed by the pipeline iteration
	 * @return The generic result type that spark can process into additional RDD
	 */
	public List<MapperResultType> initialPipelineResultsFilter(CommonAnalysisStructure cas, T workItem);
	/**
	 * Spark can parallelize the entire collection directly instead of calling the getNext()
	 * in the worklist. This is useful in cases where you want Spark to handle the retrieval of the source
	 * text for example, from a http based service during the paralellization process as opposed to materializing 
	 * the entire collection at once if you have a large amount of data to process.
	 * 
	 * @return A list of T types to create the initial RDD. If this is null or empty, then the runner will attempt to call 
	 * the getNext() in the worklist.
	 */
	public List<T> getCollection();

}
