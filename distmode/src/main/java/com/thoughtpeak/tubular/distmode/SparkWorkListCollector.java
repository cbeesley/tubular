package com.thoughtpeak.tubular.distmode;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Optional;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;
import com.thoughtpeak.tubular.distmode.functions.DataSourcePartitionDriver;
/**
 * Custom worklist that provides extra functions for operating over a spark cluster
 * 
 * @author chrisbeesley
 *
 * @param <T> - The work item class that is extended from the BaseWorkItem that is used as the source to work on 
 * through the pipeline.
 * 
 * @param <V>  - The value you want to map back to the spark driver of type V. This is for mapping all the annotations
 * down to a single object (V) so that it can be filtered or mapped.
 * 
 * If your collection in the worklist is larger than memory can hold, then consider writing out the worklist to a text file
 * format on a cluster such as hdfs, then use the text loader as a data source which uses block size partitions.
 * 
 * You must also ensure that the classes you use that are defined in instance variables implement serializable or marked as
 * transient
 * 
 * <T> - The type you want to work with in the worklist to extract annotations from
 * <U> - The type you want to use as the source id's
 * <V> - The type you want to use to hold the results
 */
public interface SparkWorkListCollector<T extends BaseWorkItem,U, V> extends WorkListDocumentCollector<T,U>{
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
	public List<V> initialPipelineResultsFilter(CommonAnalysisStructure cas, T workItem);
	
	
	/**
	 * Define your data source configuration and loading of your source data from an
	 * external source on a per partition basis instead of loading the
	 * entire source dataset and transmitting it to the cluster. By using this function, only the data source
	 * the worker needs is loaded for that particular partition its working on
	 * @param sourceIds
	 * @return
	 */
	public Iterator<T> loadDocumentsFromSource(Iterator<U> sourceIds);
	

}
