package com.thoughtpeak.tubular.core.processengine;

import java.io.Serializable;
import java.util.List;

public class PipelineConfigurationKeyValue implements Serializable {
	
	
	private static final long serialVersionUID = -318629895799939302L;

	private String key;
	
	private String value;
	
	private List<String> valueAsList;
	

	public PipelineConfigurationKeyValue(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	
	
	public PipelineConfigurationKeyValue(String key, List<String> valueAsList) {
		super();
		this.key = key;
		this.valueAsList = valueAsList;
	}



	public PipelineConfigurationKeyValue(String key){
		this.key = key;
	}
	


	public String getKey() {
		return key;
	}


	public String getValue() {
		return value;
	}
	
	public List<String> getValueAsList() {
		return valueAsList;
	}

	
	

}
