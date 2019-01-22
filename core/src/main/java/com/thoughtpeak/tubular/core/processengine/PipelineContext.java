package com.thoughtpeak.tubular.core.processengine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
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
	
	private Map<String, Map<String,GenericConfigKeyValue<?>>> annotatorConfigIndex = new HashMap<String, Map<String,GenericConfigKeyValue<?>>>();
	/**
	 * This provides a way to store string based configuration parameters in
	 * either a single string or list based strings
	 * 
	 * @param annotatorName
	 * @param keyName
	 * @param value
	 */
	public void addAnnotationConfigurationParameter(String annotatorName, String keyName, String value){
		
		annotatorConfigs.put(annotatorName, new PipelineConfigurationKeyValue(keyName,value));
		
	}
	
	public void addAnnotationConfigurationParameter(String annotatorName, String keyName, List<String> value){
		
		annotatorConfigs.put(annotatorName, new PipelineConfigurationKeyValue(keyName,value));
		
	}
	/**
	 * This configuration setting allows you to store any type of object using generic
	 * types
	 * @param annotatorName
	 * @param keyName
	 * @param value
	 */
	public <T> void addGenericConfigurationParameter(String annotatorName, String keyName, T value){
		
		if(annotatorConfigIndex.containsKey(annotatorName)){
			annotatorConfigIndex.get(annotatorName).put(keyName, new GenericConfigKeyValue<T>(keyName,value));
		}else { // need to create a new index
			Map<String, GenericConfigKeyValue<?>> newIndex = new HashMap<String,GenericConfigKeyValue<?>>();
			newIndex.put(keyName, new GenericConfigKeyValue<T>(keyName,value));
			annotatorConfigIndex.put(annotatorName, newIndex);
		}
		
		
	}
	/**
	 * This returns the configuration value for the given key
	 * The caller must ensure that they are using the correct type or a cast exception 
	 * will occur
	 * 
	 * @param annotatorName
	 * @param keyName
	 * @return An Optional value as the search may return null
	 */
	@SuppressWarnings("unchecked")
	public <T> Optional<T> getGenericConfigurationParameter(String annotatorName, String keyName){
		
		if(annotatorConfigIndex.containsKey(annotatorName)){
			if(annotatorConfigIndex.get(annotatorName).containsKey(keyName))
				return (Optional<T>) Optional.fromNullable(annotatorConfigIndex.get(annotatorName).get(keyName).getValue());
		}
		return Optional.absent();
		
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
