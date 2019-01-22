package com.thoughtpeak.tubular.tests.mocktypes;

import com.thoughtpeak.tubular.core.annotations.CasIndexField;

/**
 * This is a custom type that does not extend any of the system types
 * to test a custom index
 * 
 * @author chrisbeesley
 *
 */
public class CustomType {
	
	@CasIndexField(indexType = "DEFAULT")
	private String someTextToIndex;
	
	private String otherStuff;

	public String getSomeTextToIndex() {
		return someTextToIndex;
	}

	public void setSomeTextToIndex(String someTextToIndex) {
		this.someTextToIndex = someTextToIndex;
	}

	public String getOtherStuff() {
		return otherStuff;
	}

	public void setOtherStuff(String otherStuff) {
		this.otherStuff = otherStuff;
	}
	
	

}
