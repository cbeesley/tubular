package com.thoughtpeak.tubular.distmode.runners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;

import scala.Tuple2;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;
import com.thoughtpeak.tubular.distmode.BaseSparkRunner;
import com.thoughtpeak.tubular.distmode.SparkWorkListCollector;
import com.thoughtpeak.tubular.distmode.confs.SparkRunnerConfiguration;
import com.thoughtpeak.tubular.distmode.types.MapperResultType;

public class AnnotationCounterRunner extends BaseSparkRunner<MapperResultType> {

	private static final long serialVersionUID = -4467055995049235517L;

	public AnnotationCounterRunner(SparkRunnerConfiguration config) {
		super(config);
		
	}

	
	@Override
	protected <T extends BaseWorkItem,U> void beginJob(final Pipeline pipeline,
			final SparkWorkListCollector<T,U,MapperResultType> worklist) {
		SparkConf sparkConf = new SparkConf().setMaster(runnerConfig.getRuntimeMode()).setAppName(runnerConfig.getAppName());
		//sparkConf.set(key, value)
		JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
		
		
		//conf.setAppName(pipeline.getPipelineName());
		//final Broadcast<Pipeline> broadcastpipeline = sparkContext.broadcast(pipeline.createNewCopy());
		JavaRDD<U> input = sparkContext.parallelize(worklist.getSourceIds());
		
		JavaRDD<MapperResultType> annotations = createPartitionPipelineBasedRDD(pipeline,input,worklist);
		// Transform that maps the annotations into key/value pairs
		JavaPairRDD<String, Integer> counts = annotations.mapToPair(
				/**
				 * Find all negated positive and negated labels, add one
				 */
				new PairFunction<MapperResultType, String, Integer>() {

					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, Integer> call(MapperResultType result) {

						return new Tuple2<String, Integer>(result.getLabel(), 1);
					}
				})// start accumulating and counting up the results. The call params
				 // are whatever value is in the mapToPair function with the same key
				.reduceByKey(new Function2<Integer, Integer, Integer>() {

					private static final long serialVersionUID = -4209922556793901875L;

					@Override
					public Integer call(Integer x, Integer y) {
						return x + y;
					}
				});
		
		try {
			worklist.collectionProcessCompleted();
			worklist.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		counts.saveAsTextFile(runnerConfig.getDestinationPath());
		sparkContext.close();
		
	}

}
