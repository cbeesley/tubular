package com.thoughtpeak.tubular.distmode;

import org.apache.spark.api.java.JavaSparkContext;

import com.thoughtpeak.tubular.distmode.confs.SparkRunnerConfiguration;
/**
 * Use this class if you are running jobs using the spark scheduler within the
 * application jvm
 * 
 * In this case, the JavaSparkContext is managed by a master scheduling class upon which
 * you can pass that into this class in order to setup the job actions
 * 
 * @author chrisbeesley
 *
 * @param <V> - The result type value object to use
 */
public abstract class ManagedSparkContextRunner<V> extends BaseSparkRunner<V> {

	/**
	 * Extending classes need to take care when using this varible
	 * The spark context in this implementation is managed by a master class so care
	 * must be given to not accidently close the context for example
	 */
	protected transient JavaSparkContext sparkContext;
	
	private static final long serialVersionUID = 6276677993436477337L;

	public ManagedSparkContextRunner(SparkRunnerConfiguration config, JavaSparkContext sparkContext) {
		super(config);
		this.sparkContext = sparkContext;
		
	}
	
	

}
