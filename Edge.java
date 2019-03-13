package DiGraph_A5;

public class Edge {
    private final long idNum;
    private long weight;
    private Node sourceNode;
    private Node endNode;
    private String label;

    public Edge(long idNum, Node sourceNode, Node endNode, long weight, String label) {
        this.idNum = idNum;
        this.weight = weight;
        this.endNode = endNode;
        this.sourceNode = sourceNode;
        this.label = label;
    }

    public long getIdNum() {
        return idNum;
    }

    public String getLabel() {
        return label;
    }

    public long getWeight() {
        return weight;
    }

    public Node getSourceNode() {
        return sourceNode;
    }

    public Node getEndNode() {
        return endNode;
    }
}
