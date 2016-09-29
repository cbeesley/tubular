package com.thoughtpeak.tubular.tests.mocktypes;

import java.util.Iterator;

import com.thoughtpeak.tubular.core.annotations.Initialize;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.CoreAnnotationProcessor;

public class TaggedWord implements CoreAnnotationProcessor{
	
	@Initialize
	public void initialize(){
		System.out.println("Sentence detector initialized!");
		
	}
	
	@Override
	public void process(CommonAnalysisStructure cas) {
		
		Iterator<TokenizedWord> tokenIter = cas.getAnnotationIndexIterator(TokenizedWord.class);
		
	}

}
