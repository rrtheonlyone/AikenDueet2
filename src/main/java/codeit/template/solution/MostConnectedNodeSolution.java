package codeit.template.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Arrays;

public class MostConnectedNodeSolution {
    private static class Edge {
        String from;
        String to;

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public Edge(String from, String to) {
            this.from = from;
            this.to = to;
        }

        public static Edge parseEdge(String data) {
            String[] tokens =  data.split("->");
            return new Edge(tokens[0], tokens[1]);
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    '}';
        }
    }

    private TreeMap<String, ArrayList<Edge>> adjList;
    private HashMap<String, Integer> incomingDegree;

    public MostConnectedNodeSolution(String[] dataArray) {
        
        System.out.println(Arrays.toString(dataArray));
        System.out.println(dataArray.length);


        adjList = new TreeMap<>();
        incomingDegree = new HashMap<>();

        for (String data : dataArray) {
            addEdge(Edge.parseEdge(data));
        }
    }

    public String solve() {
        int maxConnections = -1;
        String answer = "";

        for (String vertex : adjList.keySet()) {
            int numConnections = bfs(vertex);

            if (numConnections > maxConnections) {
                maxConnections = numConnections;
                answer = vertex;
            } else if (numConnections == maxConnections && vertex.compareTo(answer) < 0) {
                answer = vertex;
            }
        }

        return answer;
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

        if (!incomingDegree.containsKey(from)) {
            incomingDegree.put(from, 0);
        }

        if (!incomingDegree.containsKey(to)) {
            incomingDegree.put(to, 0);
        }

        adjList.get(from).add(edge);
        incomingDegree.put(to, incomingDegree.get(to) + 1);
    }

    private int bfs(String source) {
        int output = 0;
        ArrayDeque<String> bfsQueue = new ArrayDeque<>();
        HashMap<String, Boolean> isVisited = new HashMap<>();
        for (String vertex : adjList.keySet()) {
            isVisited.put(vertex, false);
        }

        isVisited.put(source, true);
        bfsQueue.push(source);

        while (!bfsQueue.isEmpty()) {
            String current = bfsQueue.poll();
            ++output;

            for (Edge edge : adjList.get(current)) {
                String next = edge.to;

                if (!isVisited.get(next)) {
                    isVisited.put(next, true);
                    bfsQueue.push(next);
                }
            }
        }

        return output;
    }

    public static void main(String[] args) {
        String[] dataArray = new String[]{"A->B", "B->A"};
        MostConnectedNodeSolution mostConnectedNode = new MostConnectedNodeSolution(dataArray);

        System.out.println(mostConnectedNode.solve());
    }
}
