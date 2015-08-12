package com.thoughtpeak.tubular.distmode.types;

import java.io.Serializable;
import java.util.Map;
/**
 * This class is used by the spark mapping function to normalize results
 * from the initial cluster map operation in a way that it can construct 
 * the RDD. A reducer can then be applied using a common type.
 * 
 * In in the future, subclasses can be made to return additional types such as integers
 * 
 * @author chrisbeesley
 *
 */
public class MapperResultType implements Serializable{

	private static final long serialVersionUID = -7841852233338821492L;
	/**
	 * An id that uniquely identifies this result either on the document
	 * or annotation
	 */
	private String identifier;
	/**
	 * Stores what to label this result such as the name of
	 * the annotation or a feature name of an annotation
	 */
	private String label;
	
	private String textValue;
	
	private Number numericValue;
	
	

	public MapperResultType(String identifier, String label, Number numericValue) {
		super();
		this.identifier = identifier;
		this.label = label;
		this.numericValue = numericValue;
	}

	public MapperResultType(String identifier, String label, String textValue) {
		super();
		this.identifier = identifier;
		this.label = label;
		this.textValue = textValue;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getLabel() {
		return label;
	}

	public String getTextValue() {
		return textValue;
	}

	public Number getNumericValue() {
		return numericValue;
	}
	

	
	

}
