package com.thoughtpeak.tubular.distmode.confs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtpeak.tubular.core.ExecutionConfiguration;

public class SparkRunnerConfiguration implements ExecutionConfiguration,Serializable {
	
	private static final long serialVersionUID = -2077469675741474780L;

	private String sourceTextFilePath;
	
	private String destinationPath;
	/**
	 * The master URL to connect to, such as "local" to run locally with one
	 * thread, "local[4]" to run locally with 4 cores, or "spark://master:7077"
	 * to run on a Spark standalone cluster.
	 */
	private String runtimeMode;
	/**
	 * The number of worker threads to use during the pipeline
	 * processing phase. Not to be confused with the Spark cores setting
	 * 
	 * Default is 10
	 */
	private int numWorkerThreads = 10;
	
	/** Set a name for your application. Shown in the Spark web UI.
	 */
	private String appName;
	
	private String userId;
	
	private String shortDescription;
	/**
	 * The name of a supported document source to be used such as hbase, cassandra, sql etc
	 */
	private String sourceCollection;
	
	/**
	 * When true, tells the runner to use the work item's text field as a source for document
	 * analysis through the pipeline. When false, the runner will attempt to retrieve the
	 * text source via the configure data source using BaseWorkItem's baseIdentifiers field.
	 */
	private boolean useBaseWorkItemText = false;
	/**
	 * Set this to load property files that you need to load on the cluster
	 * They need to be located in the classpath of the jar that is deployed 
	 */
	private List<String> clusterProps;
	/**
	 * Send JAR files to distribute to the cluster using the names
	 */
	private List<String> jarFileNames;
	/**
	 * Specifiy a list of jvm parameters you want to pass to the cluster
	 * spark.executor.extraJavaOptions
	 */
	private List<String> clusterJVMParams;
	
	/**
	 * Enum for setting up labels for each config param
	 *
	 */
	public enum SparkRunnerConfig {
		
	}
	
	private Map<String,String> configParameter = new HashMap<String,String>();
	
	public String getConfigurationParameter(String key){
		
		return configParameter.get(key);
	}

	public String getSourceTextFilePath() {
		return sourceTextFilePath;
	}

	public void setSourceTextFilePath(String sourceTextFilePath) {
		this.sourceTextFilePath = sourceTextFilePath;
	}

	public String getDestinationPath() {
		return destinationPath;
	}

	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

	public String getRuntimeMode() {
		return runtimeMode;
	}

	public void setRuntimeMode(String runtimeMode) {
		this.runtimeMode = runtimeMode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSourceCollection() {
		return sourceCollection;
	}

	public void setSourceCollection(String sourceCollection) {
		this.sourceCollection = sourceCollection;
	}

	public boolean isUseBaseWorkItemText() {
		return useBaseWorkItemText;
	}

	public void setUseBaseWorkItemText(boolean useBaseWorkItemText) {
		this.useBaseWorkItemText = useBaseWorkItemText;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public int getNumWorkerThreads() {
		return numWorkerThreads;
	}

	public void setNumWorkerThreads(int numWorkerThreads) {
		this.numWorkerThreads = numWorkerThreads;
	}
	
	

}
