package com.thoughtpeak.tubular.core.container;

public interface AnnotationSupportResourceContext {
	
	
	public <T> void putResource(Class<T> type, T value);
	
	public <T> void getResource(Class<T> type); 

}
