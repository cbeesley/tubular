package com.thoughtpeak.tubular.core.worklist;

import java.io.Serializable;

/**
 * A work item class that can be extended to allow api
 * user to add meta data surrounding a document when processed
 * by the runners.
 * 
 * @author chrisbeesley
 *
 */
public abstract class BaseWorkItem implements Serializable{
	
	
	private static final long serialVersionUID = -7414253574946304250L;
	/**
	 * Optionally set a unique identifier to retrieve the document from
	 * an external source
	 */
	private String baseIdentifier;
	
	

	public BaseWorkItem(String baseIdentifier) {
		super();
		this.baseIdentifier = baseIdentifier;
	}
	
	public BaseWorkItem() {
		
	}



	/**
	 * The text that is to be processed in the annotation
	 * bin.
	 * 
	 * @return The text that is to be annotated
	 */
	public abstract String getDocumentText();

	public String getBaseIdentifier() {
		return baseIdentifier;
	}
	
	
	

}
