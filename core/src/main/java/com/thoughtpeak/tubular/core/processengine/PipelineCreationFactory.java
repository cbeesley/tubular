package com.thoughtpeak.tubular.core.processengine;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class PipelineCreationFactory extends BasePooledObjectFactory<Pipeline> {
	
	private int POOL_SIZE;
	
	private final Pipeline template;
	
	public PipelineCreationFactory(Pipeline p){
		template = p;
	}

	@Override
	public Pipeline create() throws Exception {
		
		return template.createNewCopy();
	}

	@Override
	public PooledObject<Pipeline> wrap(Pipeline pipeline) {
		
		return new DefaultPooledObject<Pipeline>(pipeline);
	}
	/**
	 * Used to do any cleanup on the returned object
	 */
	@Override
	public void passivateObject(PooledObject<Pipeline> object) {
		
	}

}
