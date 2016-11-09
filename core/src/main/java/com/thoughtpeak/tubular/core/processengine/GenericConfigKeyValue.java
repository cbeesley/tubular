package com.thoughtpeak.tubular.core.processengine;

public class GenericConfigKeyValue <T>{
	
	private String key;
	
	private T value;

	public GenericConfigKeyValue(String key, T value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	

}
