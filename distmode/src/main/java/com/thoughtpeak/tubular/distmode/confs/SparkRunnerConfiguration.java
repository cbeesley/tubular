package com.thoughtpeak.tubular.distmode.confs;

import java.io.Serializable;
import java.util.HashMap;
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
	
	/** Set a name for your application. Shown in the Spark web UI.
	 */
	private String appName;
	/**
	 * The name of a supported document source to be used such as hbase, cassandra, sql etc
	 */
	private String sourceCollection;
	/**
	 * The identifier that is the unique id for the whole collection to use.
	 * This could be in the form of a document id in which this config parm then
	 * would be the name of the field to use.
	 */
	private String sourceCollectionIdentifier;
	/**
	 * When true, tells the runner to use the work item's text field as a source for document
	 * analysis through the pipeline
	 */
	private boolean useBaseWorkItemText = false;
	
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

	public String getSourceCollectionIdentifier() {
		return sourceCollectionIdentifier;
	}

	public void setSourceCollectionIdentifier(String sourceCollectionIdentifier) {
		this.sourceCollectionIdentifier = sourceCollectionIdentifier;
	}

	public boolean isUseBaseWorkItemText() {
		return useBaseWorkItemText;
	}

	public void setUseBaseWorkItemText(boolean useBaseWorkItemText) {
		this.useBaseWorkItemText = useBaseWorkItemText;
	}
	
	

}
