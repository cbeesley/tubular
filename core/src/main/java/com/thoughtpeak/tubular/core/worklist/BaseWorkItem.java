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
	 * The text that is to be processed in the cas.
	 * 
	 * @return The text that is to be annotated
	 */
	public abstract String getDocumentText();
	
	/**
	 * This method allows for setting the initial view of the source text. The reasoning
	 * is to transform the text into another format before its gets sent to the pipeline
	 * The normal way is to create a cas view but if you have a large amount of documents to 
	 * process and you have a more efficient way to perform an operation on it than within the pipeline, then
	 * use this method. Its up the extending classes to decide to override it or not
	 * 
	 * @param text
	 */	
	public void setInitialView(String text){
		throw new IllegalArgumentException("This type does not support override the text");
	}

	public String getBaseIdentifier() {
		return baseIdentifier;
	}
	
	
	

}
