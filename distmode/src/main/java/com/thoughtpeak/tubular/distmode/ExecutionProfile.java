package com.thoughtpeak.tubular.distmode;

import java.io.Serializable;

import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
/**
 * The execution profile is what determines how the RDD's
 * and results are ran over a spark cluster. The idea
 * is to package default profiles as well as custom implementations
 * 
 * V - The type you want to return back to the spark driver/runner
 * 
 * @author chrisbeesley
 *
 */
public interface ExecutionProfile<V> extends Serializable{
	
	public <T extends BaseWorkItem> void beginJob(final Pipeline pipeline,
			final SparkWorkListCollector<T,V> worklist);

}
