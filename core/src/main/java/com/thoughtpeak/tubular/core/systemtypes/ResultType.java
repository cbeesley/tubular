package com.thoughtpeak.tubular.core.systemtypes;

import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
/**
 * Protoype class to test out spark, might be removed
 * 
 *
 */
public class ResultType extends BaseWorkItem {
	
	private String result;
	
	public ResultType(String result){
		this.result = result;
	}

	@Override
	public String getDocumentText() {
		// TODO Auto-generated method stub
		return result;
	}

}
