# Tubular
Text Processing Framework

This is a framework for creating text processing pipelines for anything from natural language processing to realtime message processing based loosely on the UIMA specification. Tubular allows you to create, test and execute text processing applications without regard for the underlying execution system. This could range from a single thread to parallel/distributed systems like Spark.

##Setup and Configuration
This project currently is not in any repos yet so you will need to clone this repository.

The first thing to create is the code that will run your processing logic using the Analysis component:

```
@AnalysisComponent(name = "TestWordTokenAnnotator" , dependsOnAnyOf = "DocumentPreProcess")
public class TestWordTokenAnnotator implements CoreAnnotationProcessor{
	
	@Initialize
	public void initialize(){
		// Code to run when the pipeline is created
		
	}
	
	@Override
	public void process(CommonAnalysisStructure cas) {
		
		// Processing logic goes here		
		
	}

}
```
The CommonAnalysisStructure is an indexed view of what previous annotators in your pipeline have added until it reaches the next in line.

### Configuration Parameters
If you need to pass configuration parameters to your Analysis Component, the recommended way is to use a PipelineContext so that parameters may be distibuted correctly on anything from a single test runner to a Spark distrubuted job:

```
PipelineContext ctx = new PipelineContext();
// Simple string based params are supported
ctx.addAnnotationConfigurationParameter(TestSentenceDetector.ANNOTATOR_NAME, "someConfigParm", "/opt/some/file.gz");

// You can use string based lists too
List<String> parmList = Lists.newArrayList("parm1","parm2,", "etc");
ctx.addAnnotationConfigurationParameter(TestSentenceDetector.ANNOTATOR_NAME, "configList", parmList);

// You can use your own types too
TestType test = new TestType();
test.setCoveredText("anotherTest");

ctx.addGenericConfigurationParameter("SentenceAnnotator", "someParm", test);

```

Then in your Analysis component, create a initialize method with the PipelineContext as a parameter:

```
@Initialize
	public void initialize(PipelineContext context){
		// You can use simple strings
		final String relationsStr = configValues.get("someConfigParm").getValue();
		
		// Getting a custom type using generics - You must ensure you are using/know the correct type
		Optional<Concept> someParm = ctx.getGenericConfigurationParameter("SentenceAnnotator", "someParm");
		
	}
	
```
#Framework Concepts 

## Pipelines

Pipelines play a central role in Tubular. The idea of a pipeline in Tubular is to allow reuse and cooperative behaviors in text processing logic similar to the pipe system in Unix systems. This allows setup, testing, and execution of text processing strategies without the need to configure an underlying system to run it. 

Each component in the pipeline is called an AnnotationProcessor. The AnnotationProcessor takes in as input the text you want to analyze or annotations produced by other AnnotationProcessors for your application to use. When your algorithm creates new annotations, they get stored back into what is called the Common Analysis Structure from the UIMA spec. When a single iteration of the pipeline is completed, you can use Tubular's worklist to extract, filter, and write out the annotations of interest using worklists.

## Worklists

Tubular can handle a multiple datasources which are configured as worklists. The worklist is a iterator based interface that allows a uniform loading of data into the pipeline depending on which mode of operation (The runners in tubular) is used. Each item in the worklist is of type BaseWorkItem because a subject of analysis such as a piece of text is required to process through the pipeline. You can subclass this class to hold meta data as its processed through the pipeline.

The strategy, format, and location on how to consume the input data is up to your application. Some examples would be:

* Local source - This could be a text file based on the file system that tubular is running on. You could create a worklist using the BasicWorkItem and a CSV parser to go through each line to populate a workitem object of your creation.

* Database - The source of the text to analyze could be in a database. You could use the iterator process to page through the resultset.

* Identifier based - You could create the worklist using some kind of unique identifier. When the getNext() method is called, the identifier is used to retrieve the text for analysis in the pipeline.

* Distributed - The source could be located in a cluster like HDFS or Cassandra. Tubular contains utilities to load these sources when its run in distributed mode

## Running Tubular in distributed mode

Tubular can run your pipeline on a cluster using Spark. While the pipelines that you create can generally remain the same, there are however some things you need to do within each annotation processor.

* Ensure that instance variables contain classes that are Serializable - Spark distributes these on a cluster by using Serialization

* If you are using third party classes that are not serialized, mark instance variables as transient or static

* You may need to synchronize access to instance variables in your annotation processors if they are static. By default, Tubular spark runner creates partitions over which one instance of the Pipeline is created which is similar to how ConcurrentRunner runs jobs using multiple threads.

To build a deployable spark job, your project needs to supply the spark cluster with all the required dependancies. One method if you are using Maven is to use the shade plugin to combine them into a single jar. Use the following snippet in your pom

            <!-- Maven shade plug-in  JARs -->
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-shade-plugin</artifactId>
				<version>2.3</version>
				<executions>
					<execution>
						<phase>package</phase>
						<goals>
							<goal>shade</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			
Then run:

	$ mvn package
	
The generated jar can then be deployed using the spark-submit script
