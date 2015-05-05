package com.thoughtpeak.tubular.core.container;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.PeekingIterator;

public interface AnnotationIndex<T> {
	/**
	 * Adds a bean class that is a feature of
	 * to the index. Depending on what type of index implementation,
	 * the add operation may create additional indexes such as count of words
	 * a positional index etc.
	 * 
	 * This operation may also depend on certain java annotations on the feature class being added
	 * 
	 * @param instance The feature of any type
	 */
	public void addAnnotationToIndex(T instance);
	/**
	 * Removes a annotation from the index. This operation relies on class equality
	 * to find the class and remove so be sure the annotation instance overrides it.
	 * @param instance
	 */
	public void removeFromIndex(T instance);
		
	public Iterator<T> getIndexIterator();
	/**
	 * Provides a "peeking" iterator to allow a kind of look ahead to examine
	 * the next element in the iteration without moving the iteration forward
	 * @return PeekingIterator instance of type
	 */
	public PeekingIterator<T> getIndexPeekingIterator();
	/**
	 * Retrieves the full index as a set that is ordered by token entry	
	 * @return A set with the given type
	 */
	public Set<T> getFullSet();
	
	public int getWordCount(T instance);
	
	public List<T> findAnnotationsInSpanRange(int startPosition, int endPosition);
	/**
	 * Finds annotations within the Type using the relative positions
	 * So if there are 5 tokens, then this index 
	 * would have keys of 1,2,3,4,5 respectively and pointing to the Type T annotation type
	 * This allows the caller to use deterministic calculations to finding annotations that are near
	 * by within the defined type.
	 * 
	 * @param startPosition
	 * @param endPosition
	 * @return
	 */
	public List<T> findAnnotationsInRelativePositionRange(int startPosition, int endPosition);
	
	

}
