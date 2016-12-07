package com.thoughtpeak.tubular.core.processengine;

import java.io.Serializable;

public class GenericConfigKeyValue <T> implements Serializable {
	
	private static final long serialVersionUID = -9018528694031020899L;

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
