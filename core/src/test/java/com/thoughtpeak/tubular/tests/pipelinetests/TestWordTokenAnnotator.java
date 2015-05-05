package com.thoughtpeak.tubular.tests.pipelinetests;

import java.text.StringCharacterIterator;
import java.util.Iterator;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.thoughtpeak.tubular.core.annotations.AnalysisComponent;
import com.thoughtpeak.tubular.core.annotations.Initialize;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.CoreAnnotationProcessor;
import com.thoughtpeak.tubular.tests.mocktypes.SentenceType;
import com.thoughtpeak.tubular.tests.mocktypes.TokenizedWord;
/**
 * A simple test tokenizer that takes the entire document text and tokenizes it
 * based on spaces, quotes, and commas as the delimeter.
 * 
 * It also downcases each token
 * 
 * @author chrisbeesley
 *
 */
@AnalysisComponent(name = "TestWordTokenAnnotator" , dependsOnAnyOf = "DocumentPreProcess")
public class TestWordTokenAnnotator implements CoreAnnotationProcessor{
	
	@Initialize
	public void initialize(){
		System.out.println("WordTokenAnnotator initialized!");
		
	}
	
	@Override
	public void process(CommonAnalysisStructure cas) {
		
		String document = cas.getSourceText();
		StringCharacterIterator iter = new StringCharacterIterator(document);
		StringBuilder buffer = new StringBuilder();
		int currentBeginIdx = 0;
		int currentEndIdx = 0;
		int pos = 0;
		
		for(char c = iter.first(); c != StringCharacterIterator.DONE; c = iter.next()) {
			
			if(!CharMatcher.anyOf(" ,\"").matches(c)){
				buffer.append(c);
				currentEndIdx = iter.getIndex();
				
			}else {
				// sometimes the buffer is empty
				if(buffer.length() == 0)
					continue;
				TokenizedWord word = new TokenizedWord();
				word.setRelativePosition(pos++);
				word.setCoveredText(buffer.toString().toLowerCase().trim());
				word.setBeginSpanPosition(currentBeginIdx);
				word.setEndSpanPosition(currentEndIdx);
				cas.addToIndex(word);
				buffer = new StringBuilder();
				currentBeginIdx = iter.getIndex();
				
			}
		}
		
		
		
	}

}
