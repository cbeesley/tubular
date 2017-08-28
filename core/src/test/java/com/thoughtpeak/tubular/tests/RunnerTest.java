package com.thoughtpeak.tubular.tests;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.thoughtpeak.tubular.core.collection.CSVCreatorChannel;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.processengine.PipelineContext;
import com.thoughtpeak.tubular.core.runners.ConcurrentRunner;
import com.thoughtpeak.tubular.core.runners.SimpleRunner;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;
import com.thoughtpeak.tubular.tests.pipelinetests.TestConceptDetector;
import com.thoughtpeak.tubular.tests.pipelinetests.TestSentenceDetector;
import com.thoughtpeak.tubular.tests.pipelinetests.TestWordTokenAnnotator;
/**
 * Does a full run through of a pipeline and channels
 * @author chrisbeesley
 *
 */
public class RunnerTest extends BaseDocumentTest {
	
	private Pipeline pipeline;
	
	private TestSentenceDetector sample = new TestSentenceDetector();
	
	@Before
	public void setUp(){
		System.out.println("##Setting up pipeline test");
		// Create a new context to test with
		PipelineContext ctx = new PipelineContext();
		ctx.addAnnotationConfigurationParameter(TestSentenceDetector.ANNOTATOR_NAME, "modelFile", "/opt/some/file.gz");
		List<String> parmList = Lists.newArrayList("parm1","parm2,", "etc");
		ctx.addAnnotationConfigurationParameter(TestSentenceDetector.ANNOTATOR_NAME, "configList", parmList);
		
		pipeline = new Pipeline.Assemble("Test Pipeline")
		.addAnalyzer(sample)
		.addAnalyzer(new TestWordTokenAnnotator())
		.addAnalyzer(new TestConceptDetector())
		.setPipelineContext(ctx)
		.create();
		
		
	}
	
	@Test
	public void annotatorConfigurationTest() {
		
		// check the annotator config is working
		assertNotNull(sample.getPipelineContext());
	}
	
	@Test
	public void simpleRunnerTest(){
		
		
		
		try {
			
			TestWorklist testWorklist = new TestWorklist(this.loadMultiDocument("multiDoc.txt"));
			SimpleRunner runner = new SimpleRunner();
			runner.execute(pipeline, testWorklist);
			
			CSVCreatorChannel csvChannel = new CSVCreatorChannel(null);
			
			System.out.println("## Completed pipeline test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void concurrentRunnerTest(){
		
		
		
		try {
			
			TestWorklist testWorklist = new TestWorklist(this.loadMultiDocument("multiDoc.txt"));
			ConcurrentRunner runner = new ConcurrentRunner();
			runner.execute(pipeline, testWorklist);
			
			CSVCreatorChannel csvChannel = new CSVCreatorChannel(null);
			
			System.out.println("## Completed pipeline test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class TestWorklist implements WorkListDocumentCollector<BaseWorkItem,BaseWorkItem> {
		
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

        @Override
        public List<BaseWorkItem> loadDocuments(List<BaseWorkItem> itemList) {
            // TODO Auto-generated method stub
            return null;
        }

		@Override
		public List<BaseWorkItem> getSourceIds() {
			// TODO Auto-generated method stub
			return null;
		}

        /**
         * @see com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector#writeResults(java.util.List)
         */
        @Override
        public <E> void writeResults(List<E> results) {
            // TODO Auto-generated method stub
        }

		@Override
		public BaseWorkItem loadDocument(BaseWorkItem item) {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
