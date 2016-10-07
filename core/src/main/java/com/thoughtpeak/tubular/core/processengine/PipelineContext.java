package com.thoughtpeak.tubular.core.processengine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

/**
 * Very basic configuration context. This is used to pass configuration and resource information to the pipeline as
 * well as to each annotator for initialization. Eventually convert to use more generic typing to handle values as
 * lists and maps
 * 
 * @author chrisbeesley
 *
 */
public class PipelineContext implements Serializable{
	
	private static final long serialVersionUID = 2587321379137703263L;
	
	private ListMultimap<String, PipelineConfigurationKeyValue> annotatorConfigs = ArrayListMultimap.create();
	
	public void addAnnotationConfigurationParameter(String annotatorName, String keyName, String value){
		
		annotatorConfigs.put(annotatorName, new PipelineConfigurationKeyValue(keyName,value));
		
	}
	
	public void addAnnotationConfigurationParameter(String annotatorName, String keyName, List<String> value){
		
		annotatorConfigs.put(annotatorName, new PipelineConfigurationKeyValue(keyName,value));
		
	}
	
	public Map<String,PipelineConfigurationKeyValue> getConfigurationValue(String annotatorName) {
		
		List<PipelineConfigurationKeyValue> annotatorConfig = annotatorConfigs.get(annotatorName);
		Map<String,PipelineConfigurationKeyValue> results = Maps.newHashMap();
		
		for(PipelineConfigurationKeyValue keyValue : annotatorConfig){
			results.put(keyValue.getKey(), keyValue);
		}
		return results;
	}
	
	private String generateKey(String annotatorName, String keyName){
		StringBuilder compositeKey = new StringBuilder();
		compositeKey.append(annotatorName);
		compositeKey.append("~");
		compositeKey.append(keyName);
		
		return compositeKey.toString();
	}

}
