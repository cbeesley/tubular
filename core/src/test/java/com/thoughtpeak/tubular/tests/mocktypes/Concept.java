package com.thoughtpeak.tubular.tests.mocktypes;

import com.thoughtpeak.tubular.core.systemtypes.BaseAnnotationType;

public class Concept extends BaseAnnotationType {
	
	private String coveredText;


	public String getCoveredText() {
		return coveredText;
	}

	public void setCoveredText(String coveredText) {
		this.coveredText = coveredText;
	}
	
	public String toString(){
		return coveredText;
	}

}
