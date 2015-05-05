package com.thoughtpeak.tubular.tests.pipelinetests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.runners.ConcurrentRunner;
import com.thoughtpeak.tubular.tests.BaseDocumentTest;
import com.thoughtpeak.tubular.tests.mocktypes.Concept;
import com.thoughtpeak.tubular.tests.mocktypes.PartOfSpeechWord;
import com.thoughtpeak.tubular.tests.mocktypes.SentenceType;
import com.thoughtpeak.tubular.tests.mocktypes.TokenizedWord;
/**
 * Tests the pipeline execution
 * @author cbeesley
 *
 */
public class TestPipeline extends BaseDocumentTest {
	
	private Pipeline pipeline;
	
	private CommonAnalysisStructure bin;
	/**
	 * Setup and run pipeline, get the annotations for a
	 * two sentence example
	 */
	@Before
	public void setUp(){
		System.out.println("##Setting up pipeline test");
		pipeline = new Pipeline.Assemble("Test Pipeline")
		.addAnalyzer(new TestSentenceDetector())
		.addAnalyzer(new TestWordTokenAnnotator())
		.addAnalyzer(new TestConceptDetector())
		.create();
		
		bin = pipeline.executePipeline(twoSentenceExample);
		
	}
	
	@Test
	public void testForGettingWords(){
		System.out.println("##Starting pipeline test");
		
		
		Iterator<TokenizedWord> wordIter = bin.getAnnotationIndexIterator(TokenizedWord.class);
		//There should be some results
		assertTrue(wordIter.hasNext());
		TokenizedWord testWord = null;
		while(wordIter.hasNext()){
			TokenizedWord word = wordIter.next();
			// Find our test word in the sample
			if(word.getCoveredText().equals("language")){
				testWord = word;
				//break;
			}
			System.out.println("Word:" + word.getCoveredText());
		}
		assertNotNull(testWord);
		
		
	}
	
	@Test
	public void testNonExistantType(){
		System.out.println("## Starting non existant type test");
		Iterator<PartOfSpeechWord> sentIter = bin.getAnnotationIndexIterator(PartOfSpeechWord.class);
		assertFalse(sentIter.hasNext());
		
	}
	
	@Test
	public void testSpanRetrieval(){
		Iterator<SentenceType> setnenceIter = bin.getAnnotationIndexIterator(SentenceType.class);
		Map<?, ?> all = bin.getAllIndexes();
		Object wha = all.get(Concept.class);
		
		// We should have concepts
		assertTrue(setnenceIter.hasNext());
		while(setnenceIter.hasNext()){
			SentenceType sentence = setnenceIter.next();
			// Get all concepts within this sentence
			List<Concept> results = bin.getAnnotationsWithinSpan(sentence , Concept.class );
			System.out.println(sentence.getCoveredText());
			System.out.println(results);
			
		}
		
	}
	
	

}
