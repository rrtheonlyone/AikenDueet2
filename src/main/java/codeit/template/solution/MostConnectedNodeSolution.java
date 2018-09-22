package codeit.template.solution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

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

        public Edge transpose() {
            return new Edge(to, from);
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
    private HashMap<String, ArrayList<Edge>> adjListTranspose;
    private HashMap<String, Integer> incomingDegree;
    private HashMap<String, Boolean> isVisited;

    private HashMap<String, Integer> sccIndexMap;
    private ArrayList<String> sccRepresentatives;
    private ArrayList<Integer> sccSizes;
    private ArrayList<Integer> sccIncomingDegree;
    private ArrayList<HashSet<Integer>> sccAdjList;

    public MostConnectedNodeSolution(String[] dataArray) {
        adjList = new HashMap<>();
        adjListTranspose = new HashMap<>();
        incomingDegree = new HashMap<>();

        for (String data : dataArray) {
            addEdge(Edge.parseEdge(data));
        }

        isVisited = new HashMap<>();

        sccAdjList = new ArrayList<>();
        sccRepresentatives = new ArrayList<>();
        sccIndexMap = new HashMap<>();
        sccSizes = new ArrayList<>();
        sccIncomingDegree = new ArrayList<>();
    }

    public String solve() {
        executeKosaraju();

        int maxConnections = -1;
        String answer = "";

        for (int sccIndex = 0; sccIndex < sccAdjList.size(); ++sccIndex) {
            if (sccIncomingDegree.get(sccIndex) > 0) {
                continue;
            }

            int numConnections = bfs(sccIndex);
            if (numConnections > maxConnections) {
                maxConnections = numConnections;
                answer = sccRepresentatives.get(sccIndex);
            } else if (numConnections == maxConnections) {
                if (sccRepresentatives.get(sccIndex).compareTo(answer) < 0) {
                    answer = sccRepresentatives.get(sccIndex);
                }
            }
        }

        return answer;
    }

    private void addEdge(Edge edge) {
        String from = edge.getFrom();
        String to = edge.getTo();

        if (!adjList.containsKey(from)) {
            adjList.put(from, new ArrayList<>());
            adjListTranspose.put(from, new ArrayList<>());
        }

        if (!adjList.containsKey(to)) {
            adjList.put(to, new ArrayList<>());
            adjListTranspose.put(to, new ArrayList<>());
        }

        if (!incomingDegree.containsKey(from)) {
            incomingDegree.put(from, 0);
        }

        if (!incomingDegree.containsKey(to)) {
            incomingDegree.put(to, 0);
        }

        adjList.get(from).add(edge);
        adjListTranspose.get(to).add(edge.transpose());

        incomingDegree.put(to, incomingDegree.get(to) + 1);
    }

    private void executeKosaraju() {
        // First pass through graph
        for (String vertex : adjList.keySet()) {
            isVisited.put(vertex, false);
        }
        ArrayList<String> topologicalOrder = new ArrayList<>();

        for (String source : adjList.keySet()) {
            if (!isVisited.get(source)) {
                dfsFirstPass(source, topologicalOrder);
            }
        }

        for (String vertex : adjList.keySet()) {
            isVisited.put(vertex, false);
        }

        int currentSccIndex = 0;
        Collections.reverse(topologicalOrder);
        for (String source : topologicalOrder) {
            if (!isVisited.get(source)) {
                dfsSecondPass(source, currentSccIndex);
                ++currentSccIndex;
            }
        }

        // Initialize SCC data structures
        for (int i = 0; i < currentSccIndex; ++i) {
            sccRepresentatives.add(null);
            sccAdjList.add(new HashSet<>());
            sccIncomingDegree.add(0);
            sccSizes.add(0);
        }

        // Count vertices in each SCC
        for (String vertex : adjList.keySet()) {
            int sccIndex = sccIndexMap.get(vertex);
            sccSizes.set(sccIndex, sccSizes.get(sccIndex) + 1);

            String currentRepresentative = sccRepresentatives.get(sccIndex);
            if (currentRepresentative == null || vertex.compareTo(currentRepresentative) < 0) {
                sccRepresentatives.set(sccIndex, vertex);
            }
        }

        // Create new edges
        for (ArrayList<Edge> adjEdges : adjList.values()) {
            for (Edge edge : adjEdges) {
                String fromVertex = edge.getFrom();
                String toVertex = edge.getTo();

                int sccIndexFrom = sccIndexMap.get(fromVertex);
                int sccIndexTo = sccIndexMap.get(toVertex);

                if (sccIndexFrom == sccIndexTo) {
                    continue;
                }

                sccAdjList.get(sccIndexFrom).add(sccIndexTo);
                sccIncomingDegree.set(sccIndexTo, sccIncomingDegree.get(sccIndexTo) + 1);
            }
        }
    }

    private void dfsFirstPass(String currentVertex, ArrayList<String> topologicalOrder) {
        isVisited.put(currentVertex, true);

        for (Edge edge : adjList.get(currentVertex)) {
            String nextVertex = edge.getTo();

            if (!isVisited.get(nextVertex)) {
                dfsFirstPass(nextVertex, topologicalOrder);
            }
        }

        topologicalOrder.add(currentVertex);
    }

    private void dfsSecondPass(String currentVertex, int currentSccIndex) {
        isVisited.put(currentVertex, true);
        sccIndexMap.put(currentVertex, currentSccIndex);

        for (Edge edge : adjListTranspose.get(currentVertex)) {
            String nextVertex = edge.getTo();

            if (!isVisited.get(nextVertex)) {
                dfsSecondPass(nextVertex, currentSccIndex);
            }
        }
    }


    private int bfs(int source) {
        int output = 0;
        ArrayDeque<Integer> bfsQueue = new ArrayDeque<>();
        BitSet isVisited = new BitSet(sccAdjList.size());

        isVisited.set(source);
        bfsQueue.push(source);

        while (!bfsQueue.isEmpty()) {
            int currentSccIndex = bfsQueue.poll();
            output += sccSizes.get(currentSccIndex);

            for (Integer nextSccIndex : sccAdjList.get(currentSccIndex)) {
                if (!isVisited.get(nextSccIndex)) {
                    isVisited.set(nextSccIndex);
                    bfsQueue.push(nextSccIndex);
                }
            }
        }

        return output;
    }

    public static void main(String[] args) {
        String[] dataArray = null;

//        try (BufferedReader reader = new BufferedReader(new FileReader("src/test-logs.txt"))) {
//            dataArray = reader.readLine().split(", ");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        dataArray = new String[]{"A->B", "B->C", "C->A", "D->B"};
        MostConnectedNodeSolution mostConnectedNode = new MostConnectedNodeSolution(dataArray);

        System.out.println(mostConnectedNode.solve());
    }
}
