package com.thoughtpeak.tubular.tests.pipelinetests;

import java.util.Iterator;

import com.thoughtpeak.tubular.core.annotations.Initialize;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.CoreAnnotationProcessor;
import com.thoughtpeak.tubular.tests.mocktypes.Concept;
import com.thoughtpeak.tubular.tests.mocktypes.TokenizedWord;
/**
 * A dummy concept detector that "identifies" a concept within
 * every four words
 * 
 * @author chrisbeesley
 *
 */
public class TestConceptDetector implements CoreAnnotationProcessor {
	
	@Initialize
	public void initialize(){
		System.out.println("Concept detector initialized!");
		
	}

	@Override
	public void process(CommonAnalysisStructure bin) {
		
		Iterator<TokenizedWord> sentIter = bin.getAnnotationIndexIterator(TokenizedWord.class);
		int count = 0;
		int relativePositionCounter = 0;
		while(sentIter.hasNext()){
			
			TokenizedWord word = sentIter.next();
			
			if(count % 4 == 0){
				Concept concept = new Concept();
				concept.setRelativePosition(relativePositionCounter++);
				concept.setBeginSpanPosition(word.getBeginSpanPosition());
				concept.setEndSpanPosition(word.getEndSpanPosition());
				concept.setCoveredText(word.getCoveredText());
				bin.addToIndex(concept);
			}
			count++;
			
		}
		
		
	}
	
	

}
