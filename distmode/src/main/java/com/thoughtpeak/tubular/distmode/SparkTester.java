package com.thoughtpeak.tubular.distmode;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
/**
 * Test class that does the word count example to ensure any transitive
 * dependancies are not causing problems while developing the pipeline to work
 * on spark
 * 
 *
 */
public class SparkTester implements Serializable{

	private static final long serialVersionUID = 6457605528751148663L;

	private String inputFile = "/Users/chrisbeesley/Documents/workspace/tubular/core/src/test/resources/testDocument.txt";

	private String outputFile = "/Users/chrisbeesley/Documents/workspace/tubular/distmode/src/test/resources/results";

	public SparkTester() {
		
		// Create a Java Spark Context
		SparkConf conf = new SparkConf().setMaster("local").setAppName("wordCount");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		// Load our input data.
		JavaRDD<String> input = sc.textFile(inputFile);
		// Split up into words.
		JavaRDD<String> words = input.flatMap(new FlatMapFunction<String, String>() {
					
			private static final long serialVersionUID = -852396122968738184L;

			public Iterable<String> call(String x) {
				return Arrays.asList(x.split(" "));
			}
				});
		// Transform into pairs and count.
		JavaPairRDD<String, Integer> counts = words.mapToPair(
				
				new PairFunction<String, String, Integer>() {
					
					private static final long serialVersionUID = 1L;
					
					@Override
					public Tuple2<String, Integer> call(String x) {
						return new Tuple2<String, Integer>(x, 1);
					}
				})
				.reduceByKey(new Function2<Integer, Integer, Integer>() {
			        
					private static final long serialVersionUID = -4209922556793901875L;
					
					@Override
					public Integer call(Integer x, Integer y) {
				           return x + y;
			           }
		});
		// Save the word count back out to a text file, causing evaluation.
		counts.saveAsTextFile(outputFile);
		sc.close();
	}

	public static void main(String... args) {
		
		new SparkTester();

	}

}
