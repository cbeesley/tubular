package com.thoughtpeak.tubular.tests.pipelinetests;


import java.text.StringCharacterIterator;
import java.util.Map;

import com.google.common.base.Optional;
import com.thoughtpeak.tubular.core.annotations.AnalysisComponent;
import com.thoughtpeak.tubular.core.annotations.Initialize;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.CoreAnnotationProcessor;
import com.thoughtpeak.tubular.core.processengine.PipelineConfigurationKeyValue;
import com.thoughtpeak.tubular.core.processengine.PipelineContext;
import com.thoughtpeak.tubular.tests.mocktypes.SentenceType;

/**
 * Simple sentence tokenizer
 * 
 *
 */
@AnalysisComponent(name = TestSentenceDetector.ANNOTATOR_NAME , dependsOnAnyOf = {"DocumentPreProcess"})
public class TestSentenceDetector implements CoreAnnotationProcessor {
	
	
	private static final long serialVersionUID = 9022123410789507716L;
	
	public static final String ANNOTATOR_NAME = "SentenceDetector";
	
	private PipelineContext pipelineContext;
	
	@Initialize
	public void initialize(PipelineContext ctx){
		pipelineContext = ctx;
		
		Map<String,PipelineConfigurationKeyValue> configValues = ctx.getConfigurationValue(ANNOTATOR_NAME);
		
		// This will throw a null exception if its absent and halt the test
		Optional.of(configValues.get("modelFile").getValue());
		Optional.of(configValues.get("configList").getValueAsList());
		
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

	/**
	 * Getter for testing the context
	 * @return
	 */
	public PipelineContext getPipelineContext() {
		return pipelineContext;
	}

	

}
