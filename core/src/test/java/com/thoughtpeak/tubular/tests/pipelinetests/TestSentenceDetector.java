package com.thoughtpeak.tubular.tests.pipelinetests;


import java.text.StringCharacterIterator;

import com.thoughtpeak.tubular.core.annotations.AnalysisComponent;
import com.thoughtpeak.tubular.core.annotations.Initialize;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.CoreAnnotationProcessor;
import com.thoughtpeak.tubular.tests.mocktypes.SentenceType;

/**
 * Simple sentence tokenizer
 * 
 *
 */
@AnalysisComponent(name = "SentenceDetector" , dependsOnAnyOf = {"DocumentPreProcess"})
public class TestSentenceDetector implements CoreAnnotationProcessor {
	
	@Initialize
	public void initialize(){
		System.out.println("Sentence detector initialized!");
		
	}
	
	@Override
	public void process(CommonAnalysisStructure bin) {
		String text = bin.getSourceText();
		StringCharacterIterator iter = new StringCharacterIterator(text);
		StringBuilder buffer = new StringBuilder();
		int currentBeginIdx = 0;
		int currentEndIdx = 0;
		int position = 0;
		for(char c = iter.first(); c != StringCharacterIterator.DONE; c = iter.next()) {
			
			if(c == '.' || c == '!' || c == '?'){
				SentenceType tag1 = new SentenceType(currentBeginIdx,currentEndIdx, position++);
				tag1.setCoveredText(buffer.toString());
				bin.addToIndex(tag1);
				buffer = new StringBuilder();
				iter.next();// [a-z][.][A-Z]
				currentBeginIdx = iter.getIndex();
			}else {
				// move the pointer ahead to next position
				currentEndIdx = iter.getIndex();
				buffer.append(c);
				
			}
	         
	         
	     }
		
	}

	

}
