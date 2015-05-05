package com.thoughtpeak.tubular.core.container;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;
import com.thoughtpeak.tubular.core.features.FeatureType;
import com.thoughtpeak.tubular.core.systemtypes.BaseAnnotationType;
/**
 * The common analysis structure is a object graph that can be interrogated by
 * different analyzers to find patterns, create new objects in the form of annotations, or
 * update current annotations.
 * 
 * The CAS lifecycle starts as a piece of text like a document, email, or maybe a message like HL7 and XML.
 * A single CAS is allocated to that source (called a subject of analysis SOFA) and different analyzers then 
 * annotate the CAS like tokenizing words, finding sentences, or converting parts of the text into annotations.
 * 
 * The annotations stored in the CAS can then be used directly in their native classes or transfered into another
 * domain object.
 * 
 *
 */
public interface CommonAnalysisStructure {
	
	public CommonAnalysisStructure getView(String casViewName);
	/**
	 * Creates a document view of the this cas. Takes whatever is set as the document
	 * text and sets the new view with it. This is so that alternative instances of the 
	 * the document can be created by maintaining consistency of the word token positions
	 * 
	 * @param documentViewName
	 * @return
	 */
	public CommonAnalysisStructure createNewDocumentView(String casViewName);
	/**
	 * Get the original source text used for this CAS
	 * 
	 * @return - Source text such as a document or any other textual message
	 */
	public String getSourceText();
	
	public Map<Class<?>, AnnotationIndex<?>> getAllIndexes();
	
	public <T> AnnotationIndex<T> getAnnotationIndex(Class<T> type);
	
	public <T> Iterator<T> getAnnotationIndexIterator(Class<T> type);
	
	/**
	 * This method queries the cas index for any annotations that are within the span
	 * of the given type;
	 * 
	 * @param target
	 * @param type
	 * @return
	 */
	public <T, V extends BaseAnnotationType> List<T> getAnnotationsWithinSpan( V valueTarget, Class<T> type );

	public <T> void addToIndex(T value);

}
