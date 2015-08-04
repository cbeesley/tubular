package com.thoughtpeak.tubular.distmode;

import java.beans.Transient;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.processengine.PipelineCreationFactory;
import com.thoughtpeak.tubular.core.runners.CoreRunner;
import com.thoughtpeak.tubular.core.systemtypes.ResultType;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;
import com.thoughtpeak.tubular.distmode.confs.SparkRunnerConfiguration;
/**
 * Main spark driver class for running pipelines over a cluster or
 * standalone
 * 
 * @author chrisbeesley
 *
 */
public class SparkRunner implements CoreRunner, Serializable{
	/**
	 * Warning - any fields here need to be serializable
	 */
	private static final long serialVersionUID = 9096346296976866816L;
	
	private SparkRunnerConfiguration runnerConfig;
	
	private transient GenericObjectPool<Pipeline> pipelinePool;
	
	//protected Logger log = Logger.getLogger(SparkRunner.class);
	
	public SparkRunner(SparkRunnerConfiguration config){
		
		runnerConfig = config;
		// config parm check for required config options
		
		
		
	}

	@Override
	public <T extends BaseWorkItem> void execute(Pipeline pipeline,
			final WorkListDocumentCollector<T> worklist) {
		
		SparkConf conf2 = new SparkConf().setMaster("local").setAppName(runnerConfig.getAppName());
		JavaSparkContext sc = new JavaSparkContext(conf2);
		
		pipelinePool = new GenericObjectPool<Pipeline>(new PipelineCreationFactory(pipeline));
		
		pipelinePool.setMaxTotal(-1);
		
		//conf.setAppName(pipeline.getPipelineName());
		
		JavaRDD<T> input = null;
		
		// if the worklist has items, then this runner will use the source text
		// in each base work item or use the identifier to get it from an external source
		if(runnerConfig.isUseBaseWorkItemText()){
			//TODO temp code until the worklist is modifed to allow the full list to be passed from the implementation
			List<T> temp = new ArrayList<T>();
			while(!worklist.isComplete()){
				temp.add(worklist.getNext());
			}
			input = sc.parallelize(temp);
			
		}
		// else then use the entire collection from the configured source
		// Load our input data either by the worklist or external source like cassandra or database.
		
		JavaRDD<T> annotations = input.flatMap(new FlatMapFunction<T, T>() {
			
			private static final long serialVersionUID = -852396122968738184L;

			public Iterable<T> call(T eachItem) {
				Pipeline pipeline = null;
				
				CommonAnalysisStructure bin = null;
				try {
					pipeline = pipelinePool.borrowObject();
					
					bin = pipeline.executePipeline(eachItem.getDocumentText());
					
					worklist.workItemCompleted(bin, eachItem);
					// Send to results collection
					


				} catch (Exception e) {
					e.printStackTrace();

				}finally {

					try {
						if (pipeline != null) {
							pipelinePool.returnObject(pipeline);
						}
					} catch (Exception e) { // catch and throw anything awry
						e.printStackTrace();
					}
				}
				return new ArrayList<T>();
			}
			
			});
		annotations.reduce(new Function2<T, T, T>() {
	        
			private static final long serialVersionUID = -4209922556793901875L;
			
			@Override
			public T call(T x, T y) {
		           return y;
	           }
		});
		
		try {
			worklist.collectionProcessCompleted();
			worklist.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		sc.close();
		
		
	}
	/**
	 * Builds an RDD that will use the worklist of work items as its source
	 * 
	 * @param pipeline
	 * @param worklist
	 * @return
	 */
	public <T extends BaseWorkItem> JavaRDD<T> rddWithWorkItemAsSource(WorkListDocumentCollector<T> worklist) {
		
		
		return null;
		
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
