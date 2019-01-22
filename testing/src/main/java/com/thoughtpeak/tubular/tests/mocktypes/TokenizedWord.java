package com.thoughtpeak.tubular.tests.mocktypes;

import com.thoughtpeak.tubular.core.systemtypes.BaseAnnotationType;


public class TokenizedWord extends BaseAnnotationType {
	
	private String coveredText;
	
	private String posTag;

	public String getPosTag() {
		return posTag;
	}

	public void setPosTag(String posTag) {
		this.posTag = posTag;
	}

	public String getCoveredText() {
		return coveredText;
	}

	public void setCoveredText(String coveredText) {
		this.coveredText = coveredText;
	}
	
	

}
