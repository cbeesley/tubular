package com.thoughtpeak.tubular.tests.mocktypes;

import com.thoughtpeak.tubular.core.annotations.FeatureTarget;
import com.thoughtpeak.tubular.core.systemtypes.BaseAnnotationType;

public class SentenceType extends BaseAnnotationType {
	
	
	@FeatureTarget
	private String coveredText;
	
	public SentenceType(){
		
	}

	public SentenceType(int beginSpan, int endSpan, int orderIndex) {
		this.setRelativePosition(orderIndex);
		this.setBeginSpanPosition(beginSpan);
		this.setEndSpanPosition(endSpan);
	}

	public String getCoveredText() {
		return coveredText;
	}

	public void setCoveredText(String coveredText) {
		this.coveredText = coveredText;
	}

	
	

	
}
