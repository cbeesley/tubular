package com.thoughtpeak.tubular.core.features;

public class AnnotationType extends BaseFeatureType{
	
	private int beginIndex;
	
	private int endIndex;
	
	private String coveredText;

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	
	public String getCoveredText(){
		return coveredText;
	}
	

}
