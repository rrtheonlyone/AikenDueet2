package codeit.template.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class MessageBroadcastSolution {
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

    private HashMap<String, ArrayList<Edge>> adjList;
    private HashMap<String, Integer> incomingDegree;
    private HashMap<String, Boolean> isVisited;

    public MessageBroadcastSolution(String[] dataArray) {
        adjList = new HashMap<>();
        incomingDegree = new HashMap<>();

        for (String data : dataArray) {
            addEdge(Edge.parseEdge(data));
        }

        isVisited = new HashMap<>();
        for (String vertex : adjList.keySet()) {
            isVisited.put(vertex, false);
        }
    }

    public ArrayList<String> solve() {
        ArrayList<String> output = new ArrayList<>();

        for (String vertex : adjList.keySet()) {
            if (!isVisited.get(vertex) && incomingDegree.get(vertex) == 0) {
                output.add(vertex);
                bfs(vertex);
            }
        }

        for (String vertex : adjList.keySet()) {
            // Just in case. The dude said it's unique, but may as well.
            if (!isVisited.get(vertex)) {
                output.add(vertex);
                bfs(vertex);
            }
        }

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

        if (!incomingDegree.containsKey(from)) {
            incomingDegree.put(from, 0);
        }

        if (!incomingDegree.containsKey(to)) {
            incomingDegree.put(to, 0);
        }

        adjList.get(from).add(edge);
        incomingDegree.put(to, incomingDegree.get(to) + 1);
    }

    private void bfs(String source) {
        ArrayDeque<String> bfsQueue = new ArrayDeque<>();
        isVisited.put(source, true);
        bfsQueue.push(source);

        while (!bfsQueue.isEmpty()) {
            String current = bfsQueue.poll();
            for (Edge edge : adjList.get(current)) {
                String next = edge.to;

                if (!isVisited.get(next)) {
                    isVisited.put(next, true);
                    bfsQueue.push(next);
                }
            }
        }
    }

    public static void main(String[] args) {
        String[] dataArray = new String[]{"A->B" , "A->C" , "B->D" , "E->F" };
        MessageBroadcastSolution messageBroadcast = new MessageBroadcastSolution(dataArray);

        System.out.println(messageBroadcast.solve());
    }

}
