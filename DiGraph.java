package DiGraph_A5;

//imports
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DiGraph implements DiGraph_Interface {
  // in here go all your data and methods for the graph
  // and the topo sort operation
	private long nodeCount;
	private long edgeCount;
	private HashMap<String, Node> nodeLabelMap = new HashMap<String, Node>();
	private HashSet<Long> edgeIDs = new HashSet<Long>();
	private HashSet<Long> nodeIDs = new HashSet<Long>();
	
  public DiGraph ( ) { // default constructor
    // explicitly include this
    // we need to have the default constructor
    // if you then write others, this one will still be there
  }
  
  @Override
  public boolean addNode(long idNum, String label) {
      // check if id is >= 0, and if idNum and label are unique before adding
      if (idNum >= 0) {
          if (!nodeLabelMap.containsKey(label) && !nodeIDs.contains(idNum)) {
              nodeLabelMap.put(label, new Node(idNum, label));
              nodeIDs.add(idNum);
              nodeCount++;
              return true;
          } else {
              return false;
          }
      } else {
          return false;
      }
  }
  
  @Override
  public boolean addEdge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
      // have to also check for if the edge from s to d already exists, eLabels don't have to be unique
      if (idNum >= 0 && nodeLabelMap.containsKey(sLabel) &&
              nodeLabelMap.containsKey(dLabel) && !edgeIDs.contains(idNum)) {
          Node sourceNode = nodeLabelMap.get(sLabel);
          Node endNode = nodeLabelMap.get(dLabel);
          Edge newEdge = new Edge(idNum, sourceNode, endNode, weight, eLabel);
          if (!sourceNode.containsEdgeBetween(endNode)) {
              // store in a hashmap where the string of the endNode maps to the outEdgeMap
              sourceNode.getOutEdgesMap().put(dLabel, newEdge);
              // store in a hashmap where the string of the sourceNode maps to the outEdgeMap
              endNode.getInEdgesMap().put(sLabel, newEdge);
              edgeIDs.add(idNum);
              edgeCount++;
              return true;
          } else {
              return false;
          }
      } else {
          return false;
      }
  }
  
  @Override
  public boolean delNode(String label){
	  if (nodeLabelMap.containsKey(label)){
		  Node removedNode = nodeLabelMap.get(label);
		  // have to update edgeCount since node is being removed
		  edgeCount = edgeCount - removedNode.getInEdgesMap().size() - removedNode.getOutEdgesMap().size();
		  removedNode.getInEdgesMap().clear();
		  removedNode.getOutEdgesMap().clear();
		  nodeLabelMap.remove(label);
		  nodeCount--;
		  return true;
	  }else{
		  return false;
	  }
  }
  
  @Override
  public boolean delEdge(String sLabel, String dLabel) {
      if (nodeLabelMap.containsKey(sLabel) && nodeLabelMap.containsKey(dLabel)) {
          Node sourceNode = nodeLabelMap.get(sLabel);
          Node endNode = nodeLabelMap.get(dLabel);
          // check to see if an edge exists between the two nodes
          if (sourceNode.containsEdgeBetween(endNode)) {
              // remove the id from the edgeIDs set
              long removedEdgeID = sourceNode.getOutEdgesMap().get(endNode.getLabel()).getIdNum();
              edgeIDs.remove(removedEdgeID);
              // find the edge in the sourceNode and endNode and remove it
              sourceNode.getOutEdgesMap().remove(endNode.getLabel());
              endNode.getInEdgesMap().remove(sourceNode.getLabel());
              // decrement edgeCount because an edge was deleted
              edgeCount--;
              return true;
          } else {
              return false;
          }
      } else {
          return false;
      }
  }

  
  @Override
  public long numNodes(){
	  return nodeCount;
  }
  
  @Override
  public long numEdges(){
	  return edgeCount;
  }
  
  @Override
  public String[] topoSort() {
      // first have to scan the graph to check for the node with 0 inEdges
      ArrayList<String> topoList = new ArrayList<String>();
      Queue<Node> noEdgeQueue = new LinkedList<Node>();

      // iterate through the graph and check each node for 0 inEdges
      for (Map.Entry<String, Node> entry : nodeLabelMap.entrySet()) {
          // if a node has 0 inEdges, add to noEdgeSet
          if (entry.getValue().getInEdgesMap().size() == 0) {
              noEdgeQueue.add(entry.getValue());
          }
      }
      // now implement Kahn's Algorithm
      while (!noEdgeQueue.isEmpty()) {
          // use poll method to return top of the queue, then add to sorted list
          Node sortedNode = noEdgeQueue.poll();
          topoList.add(sortedNode.getLabel());
          // iterate through all neighbor nodes and remove the inEdge that connected it
          // as well as check if the inDegree is then 0
          // have to use concurrentHashMap to change collection while iterating over it to still use for each loops
          for (ConcurrentHashMap.Entry<String, Edge> entry : sortedNode.getOutEdgesMap().entrySet()) {
              Node endNode = nodeLabelMap.get(entry.getKey());
              this.delEdge(sortedNode.getLabel(), endNode.getLabel());
              // if inEdgesMap of neighbor nodes is now 0, add to noEdgeQueue
              if (endNode.getInEdgesMap().size() == 0) {
                  noEdgeQueue.add(endNode);
              }
          }
      }
      // if graph has edges then return null, because there was a cycle somewhere
      if (edgeCount != 0) {
          return null;
      } else { // else return the array of labels of the sorted list
          String[] topoArray = new String[topoList.size()];
          topoArray = topoList.toArray(topoArray);
          return topoArray;
      }
  }
}
