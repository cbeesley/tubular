package com.thoughtpeak.tubular.core.worklist;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import au.com.bytecode.opencsv.CSVWriter;

import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;

/**
 * Generic worklist processor that takes a pre-defined list, processes and then writes out
 * to a csv
 * 
 * 
 *
 * @param <T>
 */
public class WorkListCSVCollector <T extends BaseWorkItem> implements WorkListDocumentCollector<T>{
	/**
	 * The pre computed worklist to process
	 */
	private List<T> worklist;
	
	private AtomicInteger countdown;
	
	private CSVWriter writer;
	
	private String path = "some path";
	
	public WorkListCSVCollector(String outputPath, String[] header, List<T> worklist) throws IOException{
		
		writer = new CSVWriter(new FileWriter(path));
	}

	@Override
	public T getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void workItemCompleted(CommonAnalysisStructure bin, T workItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collectionProcessCompleted() {
		// TODO Auto-generated method stub
		
	}

}
