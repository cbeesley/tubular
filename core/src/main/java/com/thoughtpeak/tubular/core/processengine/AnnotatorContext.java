package com.thoughtpeak.tubular.core.processengine;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class AnnotatorContext implements Serializable{

	private static final long serialVersionUID = -1733658743697225008L;
	
	private ListMultimap<String, String> annotatorConfigs = ArrayListMultimap.create();
	
	public AnnotatorContext(Map<String,String> config){
		
	}
	
	
	

}
