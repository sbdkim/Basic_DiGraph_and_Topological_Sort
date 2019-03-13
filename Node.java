package DiGraph_A5;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Node {
    private final long idNum;
    private final String label;
    private ConcurrentHashMap<String, Edge> inEdges = new ConcurrentHashMap<String, Edge>();
    private ConcurrentHashMap<String, Edge> outEdges = new ConcurrentHashMap<String, Edge>();

    public Node(long idNum, String label) {
        this.idNum = idNum;
        this.label = label;
    }

    public long getIdNum() {
        return idNum;
    }

    public String getLabel() {
        return label;
    }

    public ConcurrentHashMap<String, Edge> getInEdgesMap() {
        return inEdges;
    }

    public ConcurrentHashMap<String, Edge> getOutEdgesMap() {
        return outEdges;
    }

    public boolean containsEdgeBetween(Node endNode) {
        if (this.getOutEdgesMap().get(endNode.getLabel()) == null) {
            return false;
        } else {
            return true;
        }
    }
}
