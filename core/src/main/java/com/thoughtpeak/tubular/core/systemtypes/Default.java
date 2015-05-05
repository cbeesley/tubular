package com.thoughtpeak.tubular.core.systemtypes;

import com.thoughtpeak.tubular.core.features.FeatureType;
/**
 * Used whenever there is no other types to query and used
 * as a dummy.
 * 
 *
 */
public class Default implements FeatureType{
	
	private String text;

	@Override
	public void info() {
		// TODO Auto-generated method stub
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	
	

}
