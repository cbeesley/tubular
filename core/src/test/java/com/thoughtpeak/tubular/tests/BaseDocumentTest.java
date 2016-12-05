package com.thoughtpeak.tubular.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;

public class BaseDocumentTest {

	protected String sampleParagraph;
	
	
	
	protected final String twoSentenceExample = "Java is a computer programming language that is concurrent, class-based, object-oriented, and specifically designed to have as few implementation dependencies as possible. It is intended to let application developers \"write once, run anywhere\" (WORA), meaning that code that runs on one platform does not need to be recompiled to run on another.";

	@Before
	public void setup() {
		
		
		URL url = Resources.getResource("testDocument.txt");
		try {
			sampleParagraph = Resources.toString(url, Charsets.UTF_8);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	protected List<BaseWorkItem> loadMultiDocument(String fileName) throws IOException{
		
		final List<BaseWorkItem> multiDocument = new ArrayList<BaseWorkItem>();
		URL url = Resources.getResource(fileName);
		
		Files.readLines(
				  new File(url.getPath()),
				  Charsets.UTF_8,
				  new LineProcessor<ImmutableList<String>>() {
					 
					 
					 
				    final ImmutableList.Builder<String> builder = ImmutableList.builder();
				    StringBuilder buffer = new StringBuilder();
				    
				    @Override 
				    public boolean processLine(String line) {
				    	
				    	if(line.startsWith("##############################")){
				    		if(!Strings.isNullOrEmpty(buffer.toString())){
				    			multiDocument.add( new BaseWorkItem() {
				    				@Override
				    				public String getDocumentText(){
				    					return buffer.toString();
				    				}

									@Override
									public void setInitialView(String text) {
										// TODO Auto-generated method stub
										
									}
				    				
				    			});
				    		}
				    			
				    		
				    		buffer.setLength(0);
				    	}else {
				    		buffer.append(line);
				    	}
				    	return true;
				    }
				    
				    @Override 
				    public ImmutableList<String> getResult() {
				      return builder.build();
				    }
				 });
		
		return multiDocument;
	}

}
