package org.programmierprojekt.map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {
    private TwoDimensionalTreeNode[] nodes;// nodes[i] is the TwoDimensionalTreeNode with id i
    private List<int[]>[] graph;// graph[i] list of the neighborhood [neighbor id, cost between i
    // and neighbor id]
    private TwoDimensionalTree tree;// this is the 2-D tree
    private int length;

    /**
     * read file and build graph and nodes, then use nodes to build graph tree
     */
    public Graph(String file) {

        long start = System.nanoTime();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            // skip header
            while (line != null) {
                if (line.startsWith("#") || line.isEmpty()) {
                    line = br.readLine();

                } else {
                    break;
                }
            }
            assert line != null;
            length = Integer.parseInt(line);
            nodes = new TwoDimensionalTreeNode[length];
            graph = new List[length];
            int numEdge = Integer.parseInt(br.readLine());

            for (int i = 0; i < length; i++) {
                String[] nodeParameters = br.readLine().split(" ");
                int nodeID = Integer.parseInt(nodeParameters[0]);
                double latitude = Double.parseDouble(nodeParameters[2]);
                double longitude = Double.parseDouble(nodeParameters[3]);
                nodes[nodeID] = new TwoDimensionalTreeNode(nodeID, new double[]{latitude, longitude});
                graph[nodeID] = new ArrayList<>();
            }

            for (int i = 0; i < numEdge; i++) {
                String[] edgeParameters = br.readLine().split(" ");
                int srcIDX = Integer.parseInt(edgeParameters[0]);
                int srcIDX2 = Integer.parseInt(edgeParameters[1]);
                int cost = Integer.parseInt(edgeParameters[2]);
                graph[srcIDX].add(new int[]{srcIDX2, cost});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Build k-d tree
        long readGraph = System.nanoTime();
        System.out.println("Read Graph files takes: " + (readGraph - start) / 1e9 + "s");
        tree = new TwoDimensionalTree(nodes);
        System.out.println("Build tree takes: " + (System.nanoTime() - readGraph) / 1e9 + "s");
    }

    public int getLength() {
        return length;
    }

    /**
     * find the nearest TwoDimensionalTreeNode from the coordinate (x,y).
     *
     * @param x is the longitude of the searching point.
     * @param y is the latitude of the searching point.
     */
    public TwoDimensionalTreeNode findNearestNeighbour(double x, double y) {
        return tree.findNearestNeighbour(x, y);
    }

    /**
     * Perform dijkstra's algorithm
     *
     * @param distTo       If not null, we want one-to-all distances for start point
     * @param shortestPath If not null, the caller wants the shortest path.
     * @param start        Start point, can't be null
     * @param end          End point, can be option (for one-to-all case)
     * @return the one-to-one shortest path, null if end is not valid (for one-to-all case)
     */

    public Integer minDist(int[] distTo, List<double[]> shortestPath, int start, int end) {
        if (graph[start] == null)
            return null;
        // Perform Dijkstra
        boolean NotOneToAll = distTo == null;
        if (distTo == null) {
            //perform one-to-one and no need to return distance to
            distTo = new int[graph.length];
            Arrays.fill(distTo, Integer.MAX_VALUE);
        }
        int[] parent = new int[graph.length];
        int[] finalDistTo = distTo;
        PriorityQueue<Integer> que = new PriorityQueue<>(Comparator.comparingInt(a -> finalDistTo[a]));
        distTo[start] = 0;
        que.offer(start);
        while (!que.isEmpty()) {
            // Poll the nearest node we could reach
            Integer cur = que.poll();
            if (NotOneToAll && cur.equals(end))
                break;
            for (int[] neighbor : graph[cur]) {
                int distToNext = distTo[neighbor[0]];
                int distToCur = distTo[cur];
                int edgeCost = neighbor[1];
                // Relax the edge
                if (distToCur < Integer.MAX_VALUE && distToCur + edgeCost < distToNext) {
                    distTo[neighbor[0]] = distToCur + edgeCost;
                    if (shortestPath != null) {
                        parent[neighbor[0]] = cur;
                    }
                    // Optimization via e.g. Fibonacci Heap, supports dynamic weights
                    //que.remove(neighbor[0]);
                    // Avoid cycle automatically since any cycle would have additional non-negative
                    // cost
                    que.offer(neighbor[0]);
                }
            }
        }
        if (shortestPath != null && distTo[end] < Integer.MAX_VALUE) {
            retrievePath(shortestPath, start, end, parent);
        }
        return (end < distTo.length && end >= 0) ? distTo[end] : 0;
    }

    private void retrievePath(List<double[]> shortestPath, int start, int end, int[] pathTo) {
        int cur = end;
        List<Integer> pathNodes = new ArrayList<>();
        pathNodes.add(end);
        shortestPath.add(nodes[end].getCoordinate());
        while (cur != start) {
            pathNodes.add(0, cur);
            shortestPath.add(0, nodes[pathTo[cur]].getCoordinate());
            cur = pathTo[cur];
        }
        System.out.println(pathNodes);

    }
}
