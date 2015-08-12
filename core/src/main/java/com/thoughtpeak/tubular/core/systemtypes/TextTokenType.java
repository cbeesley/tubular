package com.thoughtpeak.tubular.core.systemtypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Utility type for representing word based annotations that have
 * various encodings. When this class is extended or used directly
 * in the CAS index, it will automatically include position information
 * provided by the base annotation type
 *
 */
public class TextTokenType extends BaseAnnotationType{
	
	
	private static final long serialVersionUID = 894770298126866670L;

	private String coveredText;
	
	private int sentencePosition;
	/**
	 * A map to store attributes of this token.
	 * They can be labels such as if this word is
	 * a numeric, date, name, and so on
	 */
	private Map<String,String> attributes = new HashMap<String,String>();
	
	private List<String> singleAttributes = new ArrayList<String>();
	
	/**
	 * An integer based attribute encoding which allows
	 * a easy way to map encodings via integer bits when you have features
	 * that are present or not. The
	 * users of this will need to implement a way to decode/encode
	 * the values. Using this encoding allows use of 
	 */
	private int attributeEncoding = 0;
	
	public TextTokenType(){
		
	}
	
	public TextTokenType(int tokenPosition, int sentencePos, int beginSpan, int endSpan){
		this.setRelativePosition(tokenPosition);
		this.setBeginSpanPosition(beginSpan);
		this.setEndSpanPosition(endSpan);
		sentencePosition = sentencePos;
	}

	public String getCoveredText() {
		return coveredText;
	}

	public void setCoveredText(String coveredText) {
		this.coveredText = coveredText;
	}

	public int getSentencePosition() {
		return sentencePosition;
	}

	public void setSentencePosition(int sentencePosition) {
		this.sentencePosition = sentencePosition;
	}
	
	public void setAttribute(String labelName,String value){
		attributes.put(labelName, value);
	}
	
	public void setAttribute(String attribute){
		singleAttributes.add(attribute);
	}
	
	public Map<String,String> getAttributes(){
		return attributes;
	}
	
	public List<String> getBinaryAttributes(){
		return singleAttributes;
	}

	public int getAttributeEncoding() {
		return attributeEncoding;
	}

	public void setAttributeEncoding(int attributeEncoding) {
		this.attributeEncoding = attributeEncoding;
	}

	
	

}
