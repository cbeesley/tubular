package com.thoughtpeak.tubular.core.processengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Graph {
	
	private Node[] allNodes;
	
	public Graph(Node[] allNodes) {
		this.allNodes = allNodes;
	}
	
	public List<Node> getExecutionList(){
		//L <- Empty list that will contain the sorted elements
	    ArrayList<Node> result = new ArrayList<Node>();

	    //S <- Set of all nodes with no incoming edges
	    HashSet<Node> S = new HashSet<Node>(); 
	    for(Node n : allNodes){
	      if(n.inEdges.size() == 0){
	        S.add(n);
	      }
	    }

	    //while S is non-empty do
	    while(!S.isEmpty()){
	      //remove a node n from S
	      Node n = S.iterator().next();
	      S.remove(n);

	      //insert n into L
	      result.add(n);

	      //for each node m with an edge e from n to m do
	      for(Iterator<Edge> it = n.outEdges.iterator();it.hasNext();){
	        //remove edge e from the graph
	        Edge e = it.next();
	        Node m = e.to;
	        it.remove();//Remove edge from n
	        m.inEdges.remove(e);//Remove edge from m

	        //if m has no other incoming edges then insert m into S
	        if(m.inEdges.isEmpty()){
	          S.add(m);
	        }
	      }
	    }
	    //Check to see if all edges are removed
	    boolean cycle = false;
	    for(Node n : allNodes){
	      if(!n.inEdges.isEmpty()){
	        cycle = true;
	        break;
	      }
	    }
	    if(cycle){
	      System.out.println("Cycle present, topological sort not possible");
	    }else{
	      System.out.println("Topological Sort: "+Arrays.toString(result.toArray()));
	    }
	    
	    return result;
	}

  public static class Node{
    public final String name;
    public final HashSet<Edge> inEdges;
    public final HashSet<Edge> outEdges;
    public Node(String name) {
      this.name = name;
      inEdges = new HashSet<Edge>();
      outEdges = new HashSet<Edge>();
    }
    public Node addEdge(Node node){
      Edge e = new Edge(this, node);
      outEdges.add(e);
      node.inEdges.add(e);
      return this;
    }
    @Override
    public String toString() {
      return name;
    }
  }

  public static class Edge{
    public final Node from;
    public final Node to;
    public Edge(Node from, Node to) {
      this.from = from;
      this.to = to;
    }
    @Override
    public boolean equals(Object obj) {
      Edge e = (Edge)obj;
      return e.from == from && e.to == to;
    }
  }
}
