package com.thoughtpeak.tubular.core.runners;

import java.util.List;

import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;

/**
 * The base interface for creating runners which handle the processing
 * of worklists and pipelines
 * 
 * @author chrisbeesley
 * @param <AnnotationBin>
 *
 */
public interface CoreRunner {
	
	public <T extends BaseWorkItem> void execute(Pipeline pipeline, WorkListDocumentCollector<T> worklist);

}
