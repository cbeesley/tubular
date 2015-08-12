package com.thoughtpeak.tubular.distmode.tests;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import com.thoughtpeak.tubular.core.collection.CSVCreatorChannel;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.runners.ConcurrentRunner;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;
import com.thoughtpeak.tubular.distmode.BaseSparkRunner;
import com.thoughtpeak.tubular.distmode.confs.SparkRunnerConfiguration;
import com.thoughtpeak.tubular.distmode.runners.AnnotationCounterRunner;
import com.thoughtpeak.tubular.tests.BaseDocumentTest;
import com.thoughtpeak.tubular.tests.pipelinetests.TestConceptDetector;
import com.thoughtpeak.tubular.tests.pipelinetests.TestSentenceDetector;
import com.thoughtpeak.tubular.tests.pipelinetests.TestWordTokenAnnotator;

public class SparkRunnerFunctionalTest extends BaseDocumentTest{
	
	private Pipeline pipeline;
	
	private SparkRunnerConfiguration config;
	
	
	@Before
	public void setUp(){
		
		config = new SparkRunnerConfiguration();
		System.out.println("##Setting up pipeline test");
		pipeline = new Pipeline.Assemble("Test Pipeline")
		.addAnalyzer(new TestSentenceDetector())
		.addAnalyzer(new TestWordTokenAnnotator())
		.addAnalyzer(new TestConceptDetector())
		.create();
		
		
	}
	
	@Test
	public void runnerTest(){
		try {
			
			TestWorklist testWorklist = new TestWorklist(this.loadMultiDocument("multiDoc.txt"));
			
			AnnotationCounterRunner runner = new AnnotationCounterRunner(config);
			runner.execute(pipeline, testWorklist);
			
			CSVCreatorChannel csvChannel = new CSVCreatorChannel(null);
			
			System.out.println("## Completed pipeline test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class TestWorklist implements WorkListDocumentCollector<BaseWorkItem> {
		
		private List<BaseWorkItem> worklist;
		
		private AtomicInteger countdown;
		
		public TestWorklist(List<BaseWorkItem> worklist){
			
			this.worklist = worklist;
			countdown = new AtomicInteger(worklist.size() - 1);
		}

		@Override
		public BaseWorkItem getNext() {
			
			int currentIndex = countdown.getAndDecrement();
			System.out.println("Working on " + currentIndex);
			return worklist.get(currentIndex);
		}

		@Override
		public boolean isComplete() {
			// if zero, we reached the end of the list
			return countdown.get() <= 0 ?  true : false;
		}

		@Override
		public void close() throws IOException {
			// no op
			
		}

		@Override
		public void workItemCompleted(CommonAnalysisStructure bin, BaseWorkItem workItem) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void collectionProcessCompleted() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
