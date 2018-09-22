package codeit.template.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class FastestPathSolution {
    private static class Edge {
        String from;
        String to;
        long weight;

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public long getWeight() {
            return weight;
        }

        public Edge(String from, String to, long weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public static Edge parseEdge(String data) {
            String[] fromAndRest =  data.split("->");
            String[] toAndWeight = fromAndRest[1].split(",");

            String from = fromAndRest[0];
            String to = toAndWeight[0];
            long weight = Long.parseLong(toAndWeight[1]);

            return new Edge(from, to, weight);
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", weight=" + weight +
                    '}';
        }
    }

    private static class DijkNode implements Comparable<DijkNode> {
        private String vertex;
        private long weight;

        public DijkNode(String vertex, long weight) {
            this.vertex = vertex;
            this.weight = weight;
        }

        public String getVertex() {
            return vertex;
        }

        public long getWeight() {
            return weight;
        }

        @Override
        public int compareTo(DijkNode dijkNode) {
            return Long.compare(this.weight, dijkNode.weight);
        }
    }

    private static final long INFINITY = (1L << 60);

    private HashMap<String, ArrayList<Edge>> adjList;
    private HashMap<String, Long> distance;
    private HashMap<String, String> parent;
    private String source;
    private String destination;

    public FastestPathSolution(String[] dataArray, String source, String destination) {
        this.source = source;
        this.destination = destination;

        adjList = new HashMap<>();
        distance = new HashMap<>();
        parent = new HashMap<>();

        for (String data : dataArray) {
            addEdge(Edge.parseEdge(data));
        }
    }

    public ArrayList<String> solve() {
        for (String vertex : adjList.keySet()) {
            distance.put(vertex, INFINITY);
        }
        for (String vertex : adjList.keySet()) {
            parent.put(vertex, null);
        }
        PriorityQueue<DijkNode> dijkstraPQ = new PriorityQueue<>();

        distance.put(source, 0L);
        dijkstraPQ.add(new DijkNode(source, 0));

        while (!dijkstraPQ.isEmpty()) {
            DijkNode current = dijkstraPQ.poll();
            String currVertex = current.vertex;
            long currDist = current.weight;

            if (currDist > distance.get(currVertex)) {
                continue;
            }
            if (currVertex.equals(destination)) {
                break;
            }

            for (Edge edge : adjList.get(currVertex)) {
                String nextVertex = edge.getTo();
                long nextDist = currDist + edge.getWeight();

                if (nextDist < distance.get(nextVertex)) {
                    distance.put(nextVertex, nextDist);
                    parent.put(nextVertex, currVertex);
                    dijkstraPQ.add(new DijkNode(nextVertex, nextDist));
                }
            }
        }

        ArrayList<String> output = new ArrayList<>();

        for (String currentVertex = destination; !currentVertex.equals(source); currentVertex = parent.get(currentVertex)) {
            output.add(currentVertex);
        }

        output.add(source);
        Collections.reverse(output);

        return output;
    }

    private void addEdge(Edge edge) {
        String from = edge.getFrom();
        String to = edge.getTo();

        if (!adjList.containsKey(from)) {
            adjList.put(from, new ArrayList<>());
        }

        if (!adjList.containsKey(to)) {
            adjList.put(to, new ArrayList<>());
        }

        adjList.get(from).add(edge);
    }

    public static void main(String[] args) {
        String[] dataArray = new String[]{"A->B,1000" , "A->C,4500" , "B->D,2000" , "B->C,1000", "E->F,4000"};
        FastestPathSolution fastestPath = new FastestPathSolution(dataArray, "A", "C");

        System.out.println(fastestPath.solve());

    }

}
