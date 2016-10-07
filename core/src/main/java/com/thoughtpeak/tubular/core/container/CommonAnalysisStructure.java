package com.thoughtpeak.tubular.core.container;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
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
	 * the document can be created by maintaining consistency of the word token positions.
	 * 
	 * If you have large documents and a lot of them, you should do any pre-processing before
	 * it gets put in the cas since having one than one copy of the source/view will consume more memory 
	 * if you have a lot of views.
	 * 
	 * @param documentViewName - The name of the view that your annotators use to access
	 * @param sourceText - An optional param that will insert the string into the view, otherwise if absent (Optional.<String> absent()) uses the text from the initial view
	 * @return A new cas that using the new view
	 */
	public CommonAnalysisStructure createNewDocumentView(String casViewName, Optional<String> sourceText);
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
	 * of the given type. It uses the given annotation type's begin and end spans to search
	 * the index for a given type within. An example would be that you have a Sentence type and you want
	 * to retrieve the words ( using say a Word type of BaseAnnotationType) within the sentence. 
	 * 
	 * The example call would be:
	 * 
	 *  List<Word>  sentenceSegmentItr = cas.getAnnotationsWithinSpan(sentenceInstance, Word.class);
	 * 
	 * @param target - This is the instance (which is a subclass of BaseAnnotationType) that you want to use 
	 *                 the boundaries of and get the target annotation within that boundary
	 * @param type - The annotation class you want to find that is within
	 * @return A list containing the type that fits within the span or an empty list if no type in that span exists
	 */
	public <T, V extends BaseAnnotationType> List<T> getAnnotationsWithinSpan( V valueTarget, Class<T> type );

	public <T> void addToIndex(T value);

}
