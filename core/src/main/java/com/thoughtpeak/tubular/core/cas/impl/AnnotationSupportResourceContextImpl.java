package com.thoughtpeak.tubular.core.cas.impl;

import com.google.common.collect.ImmutableClassToInstanceMap;
import com.thoughtpeak.tubular.core.container.AnnotationSupportResourceContext;
/**
 * Implementation class that contains any shared resources that typically
 * take a while to initialize that all instances of the annotation processors's can use.
 * 
 *
 */
public class AnnotationSupportResourceContextImpl implements AnnotationSupportResourceContext {
	
	ImmutableClassToInstanceMap<Object> HANDLERS =
		       new ImmutableClassToInstanceMap.Builder<Object>().build();

	@Override
	public <T> void putResource(Class<T> type, T value) {
		HANDLERS.put(type, value);
	}

	@Override
	public <T> void getResource(Class<T> type) {
		HANDLERS.getInstance(type);
		
	}
	
	

}
