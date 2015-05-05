package com.thoughtpeak.tubular.tests.indextests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.thoughtpeak.tubular.core.cas.impl.BaseCASImpl;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.tests.BaseDocumentTest;
import com.thoughtpeak.tubular.tests.mocktypes.CustomType;
import com.thoughtpeak.tubular.tests.mocktypes.SentenceType;
import com.thoughtpeak.tubular.tests.mocktypes.TokenizedWord;

/**
 * 
 * @author Chris Beesley
 *
 */
public class TestIndexContainer extends BaseDocumentTest {
	
	private CommonAnalysisStructure sampleCas;
	
	@Before
	public void setUp(){
		
		sampleCas = BaseCASImpl.createInstance("The quick brown fox jumped over the lazy dog.");
		
	}
	/**
	 * Tests inserting tokens such as words and a sample sentence
	 */
	@Test
	public void testInsert(){
		TokenizedWord tag1 = new TokenizedWord();
		tag1.setRelativePosition(1);
		String firstword = "First word";
		tag1.setCoveredText(firstword);
		sampleCas.addToIndex(tag1);
		
		
		TokenizedWord tag2 = new TokenizedWord();
		tag2.setRelativePosition(2);
		String secondWord = "Second word";
		tag2.setCoveredText(secondWord);
		sampleCas.addToIndex(tag2);
		
		
		
		SentenceType sent = new SentenceType();
		sent.setRelativePosition(1);
		sent.setCoveredText("Some sentence with stuff");
		sampleCas.addToIndex(sent);
		Set<String> verifySet = Sets.newHashSet(firstword,secondWord,sent.getCoveredText());
		// get the index to verify insertion
		Iterator<TokenizedWord> iter = sampleCas.getAnnotationIndexIterator(TokenizedWord.class);
		while(iter.hasNext()){
			TokenizedWord word = iter.next();
			assertTrue(verifySet.contains(word.getCoveredText()));
			//System.out.println("Word:" + word.getCoveredText());
			
		}
		
		Iterator<SentenceType> sentIter = sampleCas.getAnnotationIndexIterator(SentenceType.class);
		while(sentIter.hasNext()){
			SentenceType sentout = sentIter.next();
			assertTrue(verifySet.contains(sentout.getCoveredText()));
			//System.out.println("Sentence:" + sentout.getCoveredText());
		}
		
		
	}
	
	@Test
	public void testViewCreation(){
		TokenizedWord tag1 = new TokenizedWord();
		tag1.setRelativePosition(1);
		tag1.setCoveredText("First word");
		sampleCas.addToIndex(tag1);
		
		sampleCas.createNewDocumentView("TEST_VIEW");
		// Get the view first
		CommonAnalysisStructure newView = sampleCas.getView("TEST_VIEW");
		TokenizedWord tag2 = new TokenizedWord();
		tag2.setRelativePosition(2);
		tag2.setCoveredText("First word in other view");
		newView.addToIndex(tag2);
		// Now see if the correct view gets the right word
		Iterator<TokenizedWord> wordIterNewView = newView.getAnnotationIndexIterator(TokenizedWord.class);
		TokenizedWord word = null;
		while(wordIterNewView.hasNext()){
			word = wordIterNewView.next();
		}
		
		assertEquals("First word in other view", word.getCoveredText());
		
		// Test the original bin, make sure the values are there
		Iterator<TokenizedWord> originalBinItr = sampleCas.getAnnotationIndexIterator(TokenizedWord.class);
		word = null;
		while(originalBinItr.hasNext()){
			word = originalBinItr.next();
		}
		assertEquals("First word", word.getCoveredText());
		
	}
	
	@Test
	public void testCustomIndexingField(){
		CustomType ty = new CustomType();
		ty.setSomeTextToIndex("Test");
		ty.setOtherStuff("Other");
		sampleCas.addToIndex(ty);
		
	}
	
	public void testRemove(){
		
	}
	
	public void testListing(){
		
	}

}
