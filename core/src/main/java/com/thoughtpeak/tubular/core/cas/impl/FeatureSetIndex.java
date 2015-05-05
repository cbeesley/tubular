package com.thoughtpeak.tubular.core.cas.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.PeekingIterator;


import com.thoughtpeak.tubular.core.container.AnnotationIndex;
/**
 * An feature index that contains the annotated feature
 * of which type is specfied by the T parameter. This implementation uses
 * a set for a natural ordered list of elements.
 * 
 * A second index records the number of times a word appears
 * which is the toString. This should be a annotation that 
 * reads a property
 * 
 * @author Chris Beesley
 *
 * @param <T> - The bean class that contains the text annotations
 */
public class FeatureSetIndex<T> implements AnnotationIndex<T> {
	
	private Multiset<T> featureSet;
	/**
	 * Second index that holds the counts for items which
	 * are provided by the java annotated fields
	 */
	private Multiset<String> statsIndex;
	/**
	 * Stores a list of fields that are to be
	 * annotated as features for this index
	 */
	private List<Field> targetFeatureFields;
	/**
	 * Stores the instances of each feature, indexed by class
	 */
	private Multimap<Class<T>,T> featuresStore;
	
	private Logger log = Logger.getLogger(FeatureSetIndex.class);
	
	public FeatureSetIndex(){
		featureSet = LinkedHashMultiset.create();
		statsIndex = LinkedHashMultiset.create();
		featuresStore = ArrayListMultimap.create();
		targetFeatureFields = new ArrayList<Field>();
	}
	/**
	 * Add a feature to the index
	 */
	@Override
	public void addAnnotationToIndex(T instance){
		featureSet.add(instance);
		findFeatureTarget(instance);
		if(!targetFeatureFields.isEmpty()){
			try {
				for(Field eachField : targetFeatureFields){
					Object value = FieldUtils.readDeclaredField(instance, eachField.getName() ,true);
					if(value instanceof String)
						statsIndex.add(value.toString());
					else
						log.error("The feature that was added of type "+ instance.getClass().getName() + " has a Feature target (@FeatureTarget) defined that is not a string.");
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public void removeFromIndex(T instance) {
		featureSet.remove(instance);
		statsIndex.remove(instance.toString());
	}
	@Override
	public Iterator<T> getIndexIterator(){
		return featureSet.iterator();
	}
	@Override
	public PeekingIterator<T> getIndexPeekingIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Set<T> getFullSet(){
		return featureSet.elementSet();
	}
	
	public void printAllElements(){
		
		for(T eachType : featureSet){
			System.out.println("Type: " + eachType.toString());
		}
	}
	
	@Override
	public String toString(){
		return "Feature type:" + featureSet.getClass().getName() + "\nNumber of elements:" + featureSet.size();
		 
	}
	@Override
	public int getWordCount(T instance) {
		
		return statsIndex.count(instance.toString());
	}
	
	private void findFeatureTarget(T instance){
		if(targetFeatureFields.isEmpty()){
			Class<?> base_class = instance.getClass();
			Class<?> target_class = null; 
			while(base_class != null){
				target_class = base_class;
				findFeatureAnnoatation(target_class);
				base_class = target_class.getSuperclass();
			}
		}
		
		
	}
	
	private void findFeatureAnnoatation(Class<?> target){
		
		Field[] fields = target.getDeclaredFields();
		for (Field eachField : fields) {
			eachField.setAccessible(true);
			Annotation[] anno = eachField.getAnnotations();
			for (Annotation eachAnno : anno) {
				System.out.println(eachAnno.annotationType());
				String annoName = eachAnno.annotationType().getName();
				if(annoName.equals("com.thoughtpeak.docline.core.annotations.FeatureTarget")){
					targetFeatureFields.add(eachField);
				}
			}

		}
		
	}
	/**
	 * Not supported
	 */
	@Override
	public List<T> findAnnotationsInSpanRange(int startPosition, int endPosition) {
		throw new IllegalArgumentException("The findAnnotations function in the FeatureIndex is not supported, use a class that extends BaseTextToken");
		//return null;
	}
	@Override
	public List<T> findAnnotationsInRelativePositionRange(int startPosition,
			int endPosition) {
		throw new IllegalArgumentException("The findAnnotations function in the FeatureIndex is not supported, use a class that extends BaseTextToken");
		
	}

}
