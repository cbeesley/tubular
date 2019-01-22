package com.thoughtpeak.tubular.distmode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.processengine.PipelineCreationFactory;
import com.thoughtpeak.tubular.core.runners.ConcurrentRunner;
import com.thoughtpeak.tubular.core.runners.CoreRunner;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;
import com.thoughtpeak.tubular.distmode.confs.SparkRunnerConfiguration;
import com.thoughtpeak.tubular.distmode.confs.SparkRunnerConfiguration.RESULTS_OUTPUT_TYPE;
/**
 * Base spark driver class for extending and running pipelines over a cluster or
 * standalone mode. It main purpose is to setup configuration and setup external datasource for
 * reading and writing. Then it can be subclassed to implement the execution logic for each runner.
 * 
 * There are stock driver runners available in the @see com.thoughtpeak.tubular.distmode.runners
 * 
 * When running your pipeline using this class, spark uses Serialization for job management.
 * Therefore all fields in your annotation classes must support Serialization
 * by implementing the  Serialization interface. If it is a CoreAnnotationProcessor, this class is already
 * Serializable. If the instance is not a class you can modify (like an instance variable), then it must
 * be static or if you dont need the field during execution, mark it as transient.
 * 
 * V - The type you want to return to the mapper. This could be a single annotation type that combines
 * all of the features from other types into a single view
 * 
 * @author chrisbeesley
 *
 */
public abstract class BaseSparkRunner<V> implements CoreRunner, Serializable{
	
	private transient Logger log = Logger.getLogger(BaseSparkRunner.class);
	
	
	/**
	 * Warning - any fields here need to be serializable
	 */
	private static final long serialVersionUID = 9096346296976866816L;
	
	protected SparkRunnerConfiguration runnerConfig;
	
	private int NUM_WORKERS;
	
	/**
	 * when true, tells the runner to wait until the worklist is completed and
	 * halting the main execution thread
	 */
	private boolean waitToComplete = true;
	
	//protected Logger log = Logger.getLogger(SparkRunner.class);
	
	public BaseSparkRunner(SparkRunnerConfiguration config){
		
		runnerConfig = config;
		NUM_WORKERS = config.getNumWorkerThreads();
		
		// config parm check for required config options
		// check destination
		Preconditions.checkArgument(!Strings.isNullOrEmpty(config.getDestinationPath()),
				"A destination path needs to be specfied in the configuration");
		
	}
	/**
	 * Starts the execution of the spark job in the descendant class.
	 * @param <U>
	 * @param <U>
	 * 
	 * @param pipeline
	 * @param worklist
	 */
	protected abstract <T extends BaseWorkItem, U> void beginJob(final Pipeline pipeline,
			final SparkWorkListCollector<T,U,V> worklist);

	@Override
	public <T extends BaseWorkItem, U> void execute(final Pipeline pipeline,
			final WorkListDocumentCollector<T,U> worklist) {
		// check if this is a spark based worklist
		if (worklist instanceof SparkWorkListCollector) {
			
			beginJob(pipeline,(SparkWorkListCollector<T,U,V>)worklist);

		}

		else
			throw new IllegalArgumentException("The work list instance is not a SparkWorkListCollector, please implement that interface and retry");

	}
	

	
	/**
	 * Transform that builds an RDD that will use the Pipeline to generate annotations. It uses the spark partition RDD and applies a
	 * flat map function to flatten down the annotations into results. Each partition is divided based on the number of cores/executors
	 * that the config specifies. The annotations must then be filtered to the "V" type that you specify in the extended
	 * class so that they can be used in subsequent actions and transforms.
	 * 
	 * If the runner config UseBaseWorkItemText is true, then this transform requires
	 * the text getter in BaseWorkItem to not be null and contain the subject of analysis for the pipeline.
	 * 
	 * If its false, then this method will use the configured DataSource to attempt to retrieve the source text which could be
	 * a text file, Hbase/Cassandra, or database.
	 * 
	 * @param pipeline - A single pipeline instance. Depending on the number of cores, N instances of the pipeline are created
	 * @param worklist - The items to process. 
	 * @return A RDD that contains MapperResultTypes that the worklist implementations converts to from CAS annotations
	 */
	protected <T extends BaseWorkItem,U> JavaRDD<V> createPartitionPipelineBasedRDD( final Pipeline pipeline,
			JavaRDD<U> input,final SparkWorkListCollector<T,U,V> worklist) {
		
		
		// else then use the entire collection from the configured source
		// Load our input data either by the worklist or external source like cassandra or database.
		
		// transform that runs the pipeline over the input RDD
		JavaRDD<V> annotations = input.mapPartitions(new FlatMapFunction<Iterator<U>, V>() {
			
			private static final long serialVersionUID = -852396122968738184L;

			public Iterable<V> call(Iterator<U> partitionItems) {
				Iterator<T> loadedPartition = worklist.loadDocumentsFromSource(partitionItems);
				List<V> results = new ArrayList<V>();
				Pipeline active_pipeline = pipeline.createNewCopy();
				
				try {
					// if the config says to use the work item's text, then use it as the source
					if(runnerConfig.isUseBaseWorkItemText()){
						
					}else { // we need to use the data driver to get the source using the id
						
					}
					while(loadedPartition.hasNext()){
						T eachItem = loadedPartition.next();
						CommonAnalysisStructure cas = active_pipeline.executePipeline(eachItem.getDocumentText());
						results.addAll(worklist.initialPipelineResultsFilter(cas, eachItem));
						worklist.workItemCompleted(cas, eachItem);
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();

				}
				return results;
			}
			
			});
		return annotations;

	}
	/**
	 * Runs a pool of pipelines in a spark rdd partition 
	 * 
	 * @param pipeline - The pipeline instance you want to run docs through
	 * @param input - The input source type that represents a list of ids or docs that you want to use a a key to get
	 * documents from an external source like hadoop
	 * @param worklist - The worklist that describes the job config and how to process the annotations
	 * @return
	 */
	protected <T extends BaseWorkItem,U> JavaRDD<V> createPartitionPipelineBasedRDDAsync( final Pipeline pipeline,
			JavaRDD<U> input,final SparkWorkListCollector<T,U,V> worklist) {
		
		
		// Dont use logger inside the RDD closure as its executed on a worker and is null
		JavaRDD<V> annotations = input.mapPartitions(new FlatMapFunction<Iterator<U>, V>() {
			
			private static final long serialVersionUID = -852396122968738184L;
			

			public Iterable<V> call(Iterator<U> partitionItems) {
				
				final List<V> rresults = Collections.synchronizedList(new ArrayList<V>());
				
				// Start the loading of the source data into this partition
				Iterator<T> loadedPartition = worklist.loadDocumentsFromSource(partitionItems);
				
				ListeningExecutorService jobServicePool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(NUM_WORKERS));
				final GenericObjectPool<Pipeline> pipelinePool = new GenericObjectPool<Pipeline>(new PipelineCreationFactory(pipeline));
				// mirror the number of worker threads for now
				pipelinePool.setMaxTotal(NUM_WORKERS);
				
				
				while(loadedPartition.hasNext()){
					
					final T eachItem = loadedPartition.next();
					final ListenableFuture<List<V>> future = jobServicePool.submit(new Callable<List<V>>() {
						
						@Override
						public List<V> call() throws Exception {
							Pipeline pipeline = null;
							CommonAnalysisStructure cas = null;
							List<V> results = new ArrayList<V>();
							try {
								pipeline = pipelinePool.borrowObject();
								cas = pipeline.executePipeline(eachItem.getDocumentText());
								worklist.workItemCompleted(cas, eachItem);
								rresults.addAll(worklist.initialPipelineResultsFilter(cas, eachItem));
								

							} catch (Exception e) {
								throw new Exception("Error during pipeline processing in RDD:",e);

							}finally {
								
								try {
									if (pipeline != null) {
										pipelinePool.returnObject(pipeline);
									}
								} catch (Exception e) { // catch and throw anything awry
									
									throw new Exception("Error returning pipeline object to job pool in RDD",e);
								}
							}

							return results;//Return annotations
						}
					});// end future pool
					
					/**
					 * Adds a listener to each task which tracks its
					 * state and completion
					 */
					future.addListener(new Runnable() {
						@Override
						public void run() {
							try {
								
								future.get();

								// TODO check code here for termination flags, update status counters, etc
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							}
						}
					}, MoreExecutors.sameThreadExecutor());
				}// end partition loop
				// Tell the job pool to shutdown but not terminate pending tasks
				jobServicePool.shutdown();
				// Monitor loop to determine when the pool is finished
				while(true){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					if(jobServicePool.isTerminated()){
						break;
					}
				}
				jobServicePool.shutdownNow();
				
				
				return rresults;
			}
			
			});
		return annotations;

	}
	
	
	/**
	 * Helper method for writing out results in two formats, parquet or csv
	 * 
	 * @param results
	 * @param sparkContext
	 */
	protected void exportResultsWriter(JavaRDD<V> results,JavaSparkContext sparkContext){
		
		if(this.runnerConfig.getFileOutputType() == null){
			throw new IllegalArgumentException("A file output type needs to be set for this spark job!");
		}
		if(this.runnerConfig.getFileOutputType().equals(RESULTS_OUTPUT_TYPE.CSV)){
			// This will merge all workers specified in getRunttimeMode into a single result
			// set if writing out to file system
			// If the results are somewhat large, IE more than 2 gig this will be sloooow
			results.saveAsTextFile(runnerConfig.getDestinationPath());
			
			
		}
		
//		if(this.runnerConfig.getFileOutputType().equals(RESULTS_OUTPUT_TYPE.PARQUET)){
//			// This is to setup for writing out parquet or performing filtering
//			SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sparkContext);
//			// Convert the results into a dataframe
//			DataFrame schemaPipelineResult = sqlContext.createDataFrame(results, AnnotationResult.class);
//			
//			schemaPipelineResult.coalesce(1).write().parquet(runnerConfig.getDestinationPath());
//		}
	}

}
