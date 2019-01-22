package com.thoughtpeak.tubular.distmode.functions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
/**
 * Implement this interface to provide your function to
 * retrieve documents from your data source implementation. Since this is ran
 * on the worker nodes, it must be able to initilaize any resources such as data connectors
 * within the class outside of any resources within the worklist for example. 
 * 
 * One way to pass configuration parameters is to use the SparkConf such as user name/password
 * or url to get documents
 * 
 * @author chrisbeesley
 *
 * @param <U> - The initial input type that is a subclass of BaseWorkItem
 * @param <T> - The value type that will be mapped to for input to the mapping process and used in the worklist
 */
public interface DataSourcePartitionDriver<U extends BaseWorkItem,T extends BaseWorkItem> extends Serializable{
	/**
	 * The import function called by the SparkRunner RDD function which you can 
	 * implement the data source loading from
	 * 
	 * @param partitionItems - an iterator of type U objects that will be of the length of the partition that was allocated by Spark
	 * @return The new datatype to be passed on to the pipeline process
	 */
	List<T> importSourceData(Iterator<U> partitionItems);

}
