package com.thoughtpeak.tubular.distmode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.PairFlatMapFunction;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.runners.CoreRunner;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;
import com.thoughtpeak.tubular.distmode.confs.SparkRunnerConfiguration;
import com.thoughtpeak.tubular.distmode.types.MapperResultType;
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
 * @author chrisbeesley
 *
 */
public abstract class BaseSparkRunner implements CoreRunner, Serializable{
	/**
	 * Warning - any fields here need to be serializable
	 */
	private static final long serialVersionUID = 9096346296976866816L;
	
	protected SparkRunnerConfiguration runnerConfig;
	
	//protected Logger log = Logger.getLogger(SparkRunner.class);
	
	public BaseSparkRunner(SparkRunnerConfiguration config){
		
		runnerConfig = config;
		// config parm check for required config options
		// check destination
		Preconditions.checkArgument(!Strings.isNullOrEmpty(config.getDestinationPath()),
				"A destination path needs to be specfied in the configuration");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(config.getRuntimeMode()),
				"No setRuntimeMode - A runtime mode needs to be specified such as local, \"local[4]\" to run locally with 4 cores, or \"spark://master:7077\" to run on a Spark standalone cluster.");
	}
	/**
	 * Starts the execution of the spark job in the descendant class.
	 * 
	 * @param pipeline
	 * @param worklist
	 */
	protected abstract <T extends BaseWorkItem> void beginJob(final Pipeline pipeline,
			final SparkWorkListCollector<T> worklist);

	@Override
	public <T extends BaseWorkItem> void execute(final Pipeline pipeline,
			final WorkListDocumentCollector<T> worklist) {
		// check if this is a spark based worklist
		if (worklist instanceof SparkWorkListCollector) {
			
			beginJob(pipeline,(SparkWorkListCollector<T>)worklist);

		}

		else
			throw new IllegalArgumentException("The work list instance is not a SparkWorkListCollector, please implement this interface and retry");

	}
	

	/**
	 * Transform that builds an RDD that will use the Pipeline to generate annotations. The annotations must then
	 * be filtered to MapperResultTypes so that they can be used in subsequent actions and transforms.
	 * 
	 * If the runner config UseBaseWorkItemText is true, then this transform requires
	 * the text getter in BaseWorkItem to not be null and contain the subject of analysis for the pipeline.
	 * 
	 * If its false, then this method will use the configured DataSource to attempt to retrieve the source text which could be
	 * a text file, Hbase/Cassandra, or database.
	 * 
	 * @param pipeline - A single pipeline instance
	 * @param worklist - The items to process. 
	 * @return A RDD that contains MapperResultTypes that the worklist implementations converts to from CAS annotations
	 */
	protected <T extends BaseWorkItem> JavaRDD<MapperResultType> createPartitionPipelineBasedRDD( final Pipeline pipeline,
			JavaRDD<T> input,final SparkWorkListCollector<T> worklist) {
		
		
		// else then use the entire collection from the configured source
		// Load our input data either by the worklist or external source like cassandra or database.
		
		// transform that runs the pipeline over the input RDD
		JavaRDD<MapperResultType> annotations = input.mapPartitions(new FlatMapFunction<Iterator<T>, MapperResultType>() {
			
			private static final long serialVersionUID = -852396122968738184L;

			public Iterable<MapperResultType> call(Iterator<T> partitionItems) {
				
				List<MapperResultType> results = new ArrayList<MapperResultType>();
				Pipeline active_pipeline = pipeline.createNewCopy();
				
				try {
					// if the config says to use the work item's text, then use it as the source
					if(runnerConfig.isUseBaseWorkItemText()){
						
					}else { // we need to use the data driver to get the source using the id
						
					}
					while(partitionItems.hasNext()){
						T eachItem = partitionItems.next();
						CommonAnalysisStructure cas = active_pipeline.executePipeline(eachItem.getDocumentText());
						results = worklist.initialPipelineResultsFilter(cas, eachItem);
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
	   * Get an RDD for a given Hadoop file with an arbitrary new API InputFormat
	   * and extra configuration options to pass to the input format.
	   *
	   * @param conf Configuration for setting up the dataset. Note: This will be put into a Broadcast.
	   *             Therefore if you plan to reuse this conf to create multiple RDDs, you need to make
	   *             sure you won't modify the conf. A safe approach is always creating a new conf for
	   *             a new RDD.
	   * @param fClass Class of the InputFormat
	   * @param kClass Class of the keys
	   * @param vClass Class of the values
	   *
	   * '''Note:''' Because Hadoop's RecordReader class re-uses the same Writable object for each
	   * record, directly caching the returned RDD will create many references to the same object.
	   * If you plan to directly cache Hadoop writable objects, you should first copy them using
	   * a `map` function.
	   */
	private void getHbaseRDD(){
		//sc.newAPIHadoopRDD(conf, fClass, kClass, vClass)
	}

}
