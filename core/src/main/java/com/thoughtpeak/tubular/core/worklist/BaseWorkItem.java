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
	 * The text that is to be processed in the annotation
	 * bin.
	 * 
	 * @return The text that is to be annotated
	 */
	public abstract String getDocumentText();
	

}
