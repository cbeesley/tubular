package com.thoughtpeak.tubular.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.thoughtpeak.tubular.core.processengine.PipelineConfigurationKeyValue;
import com.thoughtpeak.tubular.core.processengine.PipelineContext;

public class ContextTest {
	
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

}
