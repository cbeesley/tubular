# Tubular
Text Processing Framework

This is a framework for creating text processing pipelines for anything from natural language processing to realtime message processing based loosely on the UIMA specification. Tubular allows you to create, test and execute text processing applications without regard for the underlying execution system. This could range from a single thread to parallel/distributed systems. 

## Pipelines

Pipelines play a central role in Tubular. The idea of a pipeline in Tubular is to allow reuse and cooperative behaviors in text processing logic similar to the pipe system in Unix systems. This allows setup, testing, and execution of text processing strategies without the need to configure an underlying system to run it. 

Each component in the pipeline is called an AnnotationProcessor. The AnnotationProcessor takes in as input the text you want to analyze or annotations produced by other AnnotationProcessors for your application to use. When your algorithm creates new annotations, they get stored back into what is called the CommonAnalysisStructure. When a single iteration of the pipeline is completed, you can use Tubular's worklist to extract and write out the annotations of interest.

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
