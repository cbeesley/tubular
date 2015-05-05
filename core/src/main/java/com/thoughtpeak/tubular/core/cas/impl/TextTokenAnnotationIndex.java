package com.thoughtpeak.tubular.core.cas.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.Range;
import com.thoughtpeak.tubular.core.container.AnnotationIndex;
import com.thoughtpeak.tubular.core.systemtypes.TextTokenType;
/**
 * A feature index that is mainly used for indexing text tokens, therefore uses
 * any class that is extended by BaseTextToken
 * An feature index that contains the annotated feature that
 * of which type is specified by the T parameter and extends the TextTokenType. This implementation uses
 * a set for a natural ordered list of elements.
 * 
 * A second index records the number of times a word appears
 * which is the covered text. 
 * 
 * @author Chris Beesley
 *
 * @param <T> - The bean class that contains the text annotations and extends TextTokenType
 */
public class TextTokenAnnotationIndex<T extends TextTokenType> implements AnnotationIndex<T> {
	
	private Multiset<T> featureSet;
	/**
	 * Second index that holds the counts for items
	 */
	private Multiset<String> statsIndex;
	
	/**
	 * This index store the begin positions of the text elements
	 */
	private Map<Integer,T> spanIndex;
	
	private Map<Integer,T> positionIndex;
	
	private Ordering<T> sortedSpanList;
	
	private Logger log = Logger.getLogger(TextTokenAnnotationIndex.class);
	
	public TextTokenAnnotationIndex(){
		featureSet = LinkedHashMultiset.create();
		statsIndex = LinkedHashMultiset.create();
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
		
		
	}
	/**
	 * Add a feature 
	 */
	@Override
	public void addAnnotationToIndex(T instance){
		
		featureSet.add(instance);
		if(instance.getCoveredText() != null){
			statsIndex.add(instance.getCoveredText());
		}
		spanIndex.put(instance.getBeginSpanPosition(), instance);
		positionIndex.put(instance.getRelativePosition(), instance);
		
		
	}
	@Override
	public void removeFromIndex(T instance) {
		featureSet.remove(instance);
		statsIndex.remove(instance.toString());
		spanIndex.remove(instance.getRelativePosition());
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
		
		return statsIndex.count(instance.getCoveredText());
	}
	/**
	 * Returns a range that contains all values greater than or equal to lower and less than or equal to upper.
	 */
	@Override
	public List<T> findAnnotationsInSpanRange(int startPosition, int endPosition) {
		Iterable<Integer> iter = Iterables.filter(spanIndex.keySet(), Range.closed(startPosition,endPosition));
		List<T> results = new LinkedList<T>();
		for(Integer eachStart : iter){
			results.add(spanIndex.get(eachStart));
		}
		
		Collections.sort(results,sortedSpanList);

		return results;
	}
	@Override
	public List<T> findAnnotationsInRelativePositionRange(int startPosition,int endPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
