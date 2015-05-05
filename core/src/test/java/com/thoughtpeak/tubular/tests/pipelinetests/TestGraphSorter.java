package com.thoughtpeak.tubular.tests.pipelinetests;

import org.junit.Test;

import com.thoughtpeak.tubular.core.processengine.Graph;
import com.thoughtpeak.tubular.core.processengine.Graph.Edge;
import com.thoughtpeak.tubular.core.processengine.Graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class TestGraphSorter {
	
	
	@Test
	public void sortGraphRunThrough(){
		
	    Node docFormatter = new Node("DocumentFormatter");
	    Node sentenceDetector = new Node("sentenceDetector");
	    Node grammarParser = new Node("GrammarParser");
	    Node posDetector = new Node("POSDetector");
	    Node conceptExtractor = new Node("ConceptExtractor");
	    Node tokenizer = new Node("Tokenizer");
	    
//	    docFormatter.addEdge(tokenizer);
//	    docFormatter.addEdge(sentenceDetector);
	    sentenceDetector.addEdge(docFormatter);
	    grammarParser.addEdge(sentenceDetector);
	    posDetector.addEdge(sentenceDetector);
	    tokenizer.addEdge(docFormatter);
	    conceptExtractor.addEdge(grammarParser);
	    
	    
	    

	    Node[] allNodes = {docFormatter,sentenceDetector,grammarParser,posDetector,conceptExtractor,tokenizer};
	    Graph g = new Graph(allNodes);
	    g.getExecutionList();
	  }

}
