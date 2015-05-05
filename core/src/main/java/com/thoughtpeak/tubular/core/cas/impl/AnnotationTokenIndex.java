package com.thoughtpeak.tubular.core.cas.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.Range;
import com.thoughtpeak.tubular.core.container.AnnotationIndex;
import com.thoughtpeak.tubular.core.systemtypes.BaseAnnotationType;


/**
 * A generic index for rapid lookup of annotations such as finding counts
 *
 * @param <T>
 */
public class AnnotationTokenIndex<T extends BaseAnnotationType> implements AnnotationIndex<T> {
	
	private Multiset<T> featureSet;
	
	private Ordering<T> sortedSpanList;
	
	private Ordering<T> sortedPositionList;
	
	/**
	 * Inverted index store the begin positions of the annotation elements. It
	 * takes the beginSpan which is the position of the token within the document
	 * and uses it as the key. One of the main uses of this index is to find annotations
	 * of different types within a given span
	 */
	private Map<Integer,T> spanIndex;
	/**
	 * Contains the index that holds position indexing which is based on the 
	 * relative position of the token. So if there are 5 tokens, then this index 
	 * would be have keys of 1,2,3,4,5 respectively and pointing to the Type T annotation type
	 * This allows the caller to use deterministic calculations to finding annotations that are near
	 * by.
	 */
	private Map<Integer,T> positionIndex;
	
	private Logger log = Logger.getLogger(TextTokenAnnotationIndex.class);
	
	
	public AnnotationTokenIndex(){
		featureSet = LinkedHashMultiset.create();
		spanIndex = new HashMap<Integer,T>();
		positionIndex = new HashMap<Integer,T>();
		sortedSpanList = new Ordering<T>() {
		    @Override
		    public int compare(T left, T right) {
		    	
		    	if(left.getBeginSpanPosition() > right.getBeginSpanPosition()) {
		    	     return 1;
		    	 } else if(left.getBeginSpanPosition() < right.getBeginSpanPosition()) {
		    	     return -1;
		    	 } else {
		    	     return 0;
		    	 }
		    }
		};
		
		sortedPositionList = new Ordering<T>() {
		    @Override
		    public int compare(T left, T right) {
		    	
		    	if(left.getRelativePosition() > right.getRelativePosition()) {
		    	     return 1;
		    	 } else if(left.getRelativePosition() < right.getRelativePosition()) {
		    	     return -1;
		    	 } else {
		    	     return 0;
		    	 }
		    }
		};
		
		
	}
	/**
	 * Add a feature to the index. The index position that is
	 * used is the getBeginSpanPosition() which is usually guaranteed to
	 * be unique
	 */
	@Override
	public void addAnnotationToIndex(T instance){
		
		featureSet.add(instance);
		
		spanIndex.put(instance.getBeginSpanPosition(), instance);
		positionIndex.put(instance.getRelativePosition(), instance);
		
		
	}
	@Override
	public void removeFromIndex(T instance) {
		featureSet.remove(instance);
		spanIndex.remove(instance.getBeginSpanPosition());
		positionIndex.remove(instance.getRelativePosition());
	}
	@Override
	public Iterator<T> getIndexIterator(){
		
		return featureSet.iterator();
	}
	@Override
	public PeekingIterator<T> getIndexPeekingIterator() {
		return Iterators.peekingIterator(featureSet.iterator());
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
		
		return featureSet.count(instance);
	}
	/**
	 * Finds annotations that are within a range for this index
	 * 
	 * Returns a sorted range that contains all values greater than or equal to lower and less than or equal to upper.
	 */
	@Override
	public List<T> findAnnotationsInSpanRange(int startPosition, int endPosition) {
		Iterable<Integer> iter = Iterables.filter(spanIndex.keySet(), Range.closed(startPosition,endPosition));
		//Collections.sort(iter,)
		List<T> results = new LinkedList<T>();
		for(Integer eachStart : iter){
			results.add(spanIndex.get(eachStart));
		}
		Collections.sort(results,sortedSpanList);

		return results;
	}
	@Override
	public List<T> findAnnotationsInRelativePositionRange(int startPosition, int endPosition) {
		
		Iterable<Integer> iter = Iterables.filter(positionIndex.keySet(), Range.closed(startPosition,endPosition));
		List<T> results = new LinkedList<T>();
		for(Integer eachStart : iter){
			results.add(positionIndex.get(eachStart));
		}
		Collections.sort(results,sortedPositionList);
		
		return results;
	}
}
