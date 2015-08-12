package com.thoughtpeak.tubular.core.systemtypes;

import java.io.Serializable;

/**
 * This is the base annotation type that most
 * Annotation types should extend. When it is
 * indexed, the begin span position is used to compare and lookup
 * the type
 * 
 */
public class BaseAnnotationType implements Serializable {
	
	private static final long serialVersionUID = 8180219529631765607L;
	/**
	 * This is the relative position index
	 * which denotes what order this token was put for this type. Subclasses
	 * and annotation engines need to handle incrementing
	 * this value
	 * 
	 */
	private int relativePosition;
	/**
	 * During indexing, the beginPosition is used to refer
	 * to the annotation since typically the natural ordering
	 * makes this unique
	 * 
	 */
	private int beginSpanPosition;
	
	private int endSpanPosition;
	

	public int getRelativePosition() {
		return relativePosition;
	}
	
	public void setRelativePosition(int rel_position) {
		relativePosition = rel_position;
	}


	public int getBeginSpanPosition() {
		return beginSpanPosition;
	}

	public void setBeginSpanPosition(int beginSpanPosition) {
		this.beginSpanPosition = beginSpanPosition;
	}

	public int getEndSpanPosition() {
		return endSpanPosition;
	}

	public void setEndSpanPosition(int endSpanPosition) {
		this.endSpanPosition = endSpanPosition;
	}
	
	

}
