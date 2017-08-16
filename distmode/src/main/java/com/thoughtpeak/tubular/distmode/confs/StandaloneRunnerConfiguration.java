package com.thoughtpeak.tubular.distmode.confs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.thoughtpeak.tubular.core.StandaloneConfiguration;

/**
 *
 */
public class StandaloneRunnerConfiguration implements StandaloneConfiguration, Serializable {
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 4204120437071349713L;
    
    private String sourceTextFilePath;
    
    private String destinationPath;
    
    /**
     * Set a name for your application. Shown in the Spark web UI.
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
     * analysis through the pipeline. When false, the runner will attempt to retrieve the text
     * source via the configure data source using BaseWorkItem's baseIdentifiers field.
     */
    private boolean useBaseWorkItemText = false;
    
    private Map<String, String> configParameter = new HashMap<String, String>();
    
    public void setConfigurationParameter(String key, String value) {
        configParameter.put(key, value);
    }
    
    public String getConfigurationParameter(String key) {
        
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
}
