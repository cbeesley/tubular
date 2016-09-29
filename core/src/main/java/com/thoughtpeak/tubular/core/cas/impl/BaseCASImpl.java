package com.thoughtpeak.tubular.core.cas.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thoughtpeak.tubular.core.annotations.CasIndexField;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.container.AnnotationIndex;
import com.thoughtpeak.tubular.core.systemtypes.BaseAnnotationType;
import com.thoughtpeak.tubular.core.systemtypes.TextTokenType;
//import com.thoughtpeak.tubular.core.systemtypes.TextTokenType;
/**
 * An implementation of the common analysis structure
 * 
 * @author chrisbeesley
 *
 */
public class BaseCASImpl implements CommonAnalysisStructure {
	
	public static final String DEFAULT_VIEW_NAME = "InitialView";
	/**
	 * A class mapping of annotation indexes to use for querying
	 */
	private Map<Class<?>,AnnotationIndex<?>> annotationIndex;
	private Map<String,CommonAnalysisStructure> viewIndex;
	private Logger log = Logger.getLogger(BaseCASImpl.class);
	
	/**
	 * Contains the analyzed document text. 
	 */
	private final String sofaText;
	
	private BaseCASImpl(String textToAnalyze){
		sofaText = textToAnalyze;
	}
	/**
	 * Creates the index and view for this cas
	 */
	public void init(){
		log.debug("Creating new common analysis structure");
		//viewIndex = new HashMap<String,Map<Class<?>,FeatureIndex<?>>>();
		viewIndex = new HashMap<String,CommonAnalysisStructure>();
		annotationIndex = new HashMap<Class<?>, AnnotationIndex<?>>();
		//this.addToIndex(new Default());
		
	}

	@Override
	public CommonAnalysisStructure getView(String binViewName) {
		if(viewIndex.containsKey(binViewName)){
			 return viewIndex.get(binViewName);
		}else { // Return the default view
			log.error("There is no view with the name " + binViewName + " stored");
			return this;
		}
		

	}

	@Override
	public CommonAnalysisStructure createNewDocumentView(String casViewName) {
		if(viewIndex.containsKey(casViewName)){
			log.info("Already a view with this name, returning that view");
			return viewIndex.get(casViewName);
		}
		CommonAnalysisStructure cas = createInstance(sofaText);
		viewIndex.put(casViewName, cas);
		return cas;
	}

	@Override
	public String getSourceText() {
		return sofaText;
	}

	@Override
	public <T> AnnotationIndex<T> getAnnotationIndex(Class<T> type) {
		
		AnnotationIndex<T> index = this.getIndex(type);
		
		return index;
	}
	
	@Override
	public <T> Iterator<T> getAnnotationIndexIterator(Class<T> type) {
		AnnotationIndex<T> index = this.getIndex(type);
		return index.getIndexIterator();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void addToIndex(T value){
		
		Class<T> typeSig = (Class<T>) value.getClass();
		
		if(annotationIndex.containsKey(typeSig)){
			AnnotationIndex<T> index = (AnnotationIndex<T>) annotationIndex.get(typeSig);
			index.addAnnotationToIndex(value);
		}else { // We have a new type that has not been indexed so create new index
			if (value instanceof TextTokenType){
				AnnotationIndex<TextTokenType> index = new TextTokenAnnotationIndex<TextTokenType>();
				index.addAnnotationToIndex((TextTokenType)value);
				this.putIndex(typeSig, index);
			// Otherwise use the generic index for user defined index
			// also look for index java annotations in the class variables to setup the indexing
			}// Create index for base annotation
			else if(value instanceof BaseAnnotationType){
				AnnotationIndex<BaseAnnotationType> index = new AnnotationTokenIndex<BaseAnnotationType>();
				index.addAnnotationToIndex((BaseAnnotationType)value);
				this.putIndex(typeSig, index);
				
			// Otherwise use the generic index for user defined index
			// also look for index java annotations in the class variables to setup the indexing
			}else {
				Field[] fields = typeSig.getDeclaredFields();
				for(Field eachField : fields){
					eachField.setAccessible(true);
					Annotation[] anno = eachField.getAnnotations();
					for (Annotation eachAnno : anno) {
						if(eachAnno.annotationType().getName().equals(CasIndexField.class.getName())){
							CasIndexField field = (CasIndexField)eachAnno;
							
						}
					}
					
				}
				AnnotationIndex<T> index = new FeatureSetIndex<T>();
				index.addAnnotationToIndex(value);
				this.putIndex(typeSig, index);
			}
			//FeatureIndex<T> index = new TextTokenFeatureIndex<T>();
			
		}
	}
	

	@Override
	public Map<Class<?>, AnnotationIndex<?>> getAllIndexes() {
		return annotationIndex;
	}
	
	@Override
	public <T, V extends BaseAnnotationType> List<T> getAnnotationsWithinSpan( V valueTarget, Class<T> type ){
		AnnotationIndex<T> index = this.getIndex(type);
		// there might be an empty index. If thats the case, then we need to 
		// check if its empty. The current contract returns a blank generic index
		if( index.getFullSet().isEmpty()){
			log.debug("No index for the following type:" + type.getName());
			return new ArrayList<T>();
		}
		return index.findAnnotationsInSpanRange(valueTarget.getBeginSpanPosition(), valueTarget.getEndSpanPosition());
		
	}
	
	private <T> void putIndex(Class<T> type, AnnotationIndex<?> instance){
		if(type == null){
			throw new NullPointerException("The class type that was inserted into the annotation index is null."); 
		}
		annotationIndex.put(type, instance);
	}
	/**
	 * Retrieves the index of the given type. If no index is found, then
	 * a blank index using the generic type is returned. The caller then needs
	 * to test and handle an empty index to avoid calling unsupported functions
	 * 
	 * @param type - The target type to look for
	 * @return - the index of the types
	 */
	private <T> AnnotationIndex<T> getIndex(Class<T> type){
		
		@SuppressWarnings("unchecked")
		AnnotationIndex<T> indexg = (AnnotationIndex<T>)annotationIndex.get(type);
		if(indexg == null){
			log.debug("No results for the following type:" + type.getName());
			// return a blank set
			return new FeatureSetIndex<T>();
		}
				
		return indexg;
				
	}
	/**
	 * factory method for creating an instance of this cas
	 * @param docText - The document text to set for the initial view
	 * @return
	 */
	public static CommonAnalysisStructure createInstance(String docText){
		BaseCASImpl cas = new BaseCASImpl(docText);
		cas.init();
		return cas;
		
	}
	
	

}
