package com.thoughtpeak.tubular.distmode.functions;

import org.apache.spark.api.java.function.FlatMapFunction;

/**
 * Function that is used to identify each document artifact boundary for use
 * in each annotation processor
 * 
 * @author chrisbeesley
 *
 */
public class DocumentMappingFunction implements FlatMapFunction<String, String> {

	private static final long serialVersionUID = -8479500243169555786L;

	@Override
	public Iterable<String> call(String t) throws Exception {
		
		return null;
	}

}
