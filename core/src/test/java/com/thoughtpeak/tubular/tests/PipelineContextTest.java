package com.thoughtpeak.tubular.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.thoughtpeak.tubular.core.processengine.PipelineConfigurationKeyValue;
import com.thoughtpeak.tubular.core.processengine.PipelineContext;
import com.thoughtpeak.tubular.tests.mocktypes.Concept;
import com.thoughtpeak.tubular.tests.mocktypes.PartOfSpeechWord;
import com.thoughtpeak.tubular.tests.mocktypes.TestType;

public class PipelineContextTest {
	
	@Test
	public void testContextSetup(){
		
		PipelineContext p_ctx = new PipelineContext();
		
		// Basic parm
		p_ctx.addAnnotationConfigurationParameter("SentenceAnnotator", "modelFile", "/opt/some/file.gz");
		
		// How about lists
		List<String> parmList = Lists.newArrayList("parm1","parm2,", "etc");
		
		p_ctx.addAnnotationConfigurationParameter("SentenceAnnotator", "configList", parmList);
		
		Map<String,PipelineConfigurationKeyValue> configValues = p_ctx.getConfigurationValue("SentenceAnnotator");
		
		assertTrue(configValues.get("modelFile").getValue().equals("/opt/some/file.gz"));
		
		
		
		assertTrue(!configValues.get("configList").getValueAsList().isEmpty());
		
	}
	
	@Test
	public void testGenericsContextConfig(){
		
		PipelineContext ctx = new PipelineContext();
		Concept concept = new Concept();
		concept.setCoveredText("testText");
		TestType test = new TestType();
		test.setCoveredText("anotherTest");
		// adding some mock type into the context
		ctx.addGenericConfigurationParameter("SentenceAnnotator", "someParm", concept);
		ctx.addGenericConfigurationParameter("SentenceAnnotator", "anotherParm", test);
		
		Optional<Concept> someParm = ctx.getGenericConfigurationParameter("SentenceAnnotator", "someParm");
		Optional<TestType> anotherParm = ctx.getGenericConfigurationParameter("SentenceAnnotator", "anotherParm");
		assertTrue(someParm.isPresent());
		assertTrue(someParm.get().getCoveredText().equals("testText"));
		
		assertTrue(anotherParm.isPresent());
		assertTrue(anotherParm.get().getCoveredText().equals("anotherTest"));
		// Try a missing parm
		Optional<Concept> missingParm = ctx.getGenericConfigurationParameter("SentenceAnnotator", "missingParm");
		assertFalse(missingParm.isPresent());
		// Try a missing annotator config
		Optional<Concept> missingAnnotator = ctx.getGenericConfigurationParameter("MissingAnnotator", "missingParm");
		assertFalse(missingAnnotator.isPresent());
	}

}
