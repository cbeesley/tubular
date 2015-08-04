package com.thoughtpeak.tubular.core.systemtypes;
/**
 * A token coordinate is a immutable unit that can be a word, sentence, collection of words or annotations
 * that can be expressed by location in a single document for example. By using a combination of
 * a collection wide unique identifier such as a GUID or corpus specific id, and the position it occurs
 * in that artifact, this can be used as a composite key to retrieve a specfic annoation among a 
 * collection.
 *  
 * 
 * @author chrisbeesley
 *
 */
public class TokenCoordinate {
	
	private String collectionUniqueId;
	
	private long position = -1;
	
	private long beginSpanPosition = -1;
	
	private long endSpanPosition = -1;

	public TokenCoordinate(String collectionUniqueId, long position,
			long beginSpanPosition, long endSpanPosition) {
		super();
		this.collectionUniqueId = collectionUniqueId;
		this.position = position;
		this.beginSpanPosition = beginSpanPosition;
		this.endSpanPosition = endSpanPosition;
	}

	public TokenCoordinate(String collectionUniqueId, long position) {
		super();
		this.collectionUniqueId = collectionUniqueId;
		this.position = position;
	}

	public String getCollectionUniqueId() {
		return collectionUniqueId;
	}

	public long getPosition() {
		return position;
	}

	public long getBeginSpanPosition() {
		return beginSpanPosition;
	}

	public long getEndSpanPosition() {
		return endSpanPosition;
	}

}
