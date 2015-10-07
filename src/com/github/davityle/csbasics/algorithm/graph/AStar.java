package com.github.davityle.csbasics.algorithm.graph;

import com.github.davityle.csbasics.data.graph.Graph;
import com.github.davityle.csbasics.data.heap.BinaryHeap;

import java.util.*;

public class AStar {

    private static final class Node<T, V extends Graph.Vertex<T>> implements Comparable<Node<T, V>> {
        private final V vertex;
        private final double distance;
        private final double estimatedLength;
        private final Node<T, V> previous;

        private Node(V vertex, double distance, double heuristic, Node<T, V> previous) {
            this.vertex = vertex;
            this.distance = distance;
            this.estimatedLength = distance + heuristic;
            this.previous = previous;
        }

        @Override
        public int hashCode() {
            return vertex.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node))
                return false;
            Node n = (Node) obj;
            return vertex.equals(n.vertex);
        }

        @Override
        public int compareTo(Node<T, V> n) {
            if (estimatedLength > n.estimatedLength)
                return 1;
            if (estimatedLength < n.estimatedLength)
                return -1;
            return 0;
        }

        @Override
        public String toString() {
            return String.format("%.4f - %s", estimatedLength, vertex);
        }
    }


    public static <T, V extends Graph.Vertex<T>> Optional<List<V>> shortestRoute(V start, V end, Graph<T, V> graph) {
        BinaryHeap<Node<T, V>> priorityQueue = new BinaryHeap<>();
        Set<Node<T, V>> visited = new HashSet<>();

        priorityQueue.add(new Node<>(start, 0, 0, null));
        while (!priorityQueue.isEmpty()) {
            final Node<T, V> node = priorityQueue.poll();
            if (visited.add(node)) {
                for (V vertex : graph.getAdjacentVertices(node.vertex)) {

                    Node<T, V> nextNode = new Node<>(vertex, node.distance + length(node.vertex, vertex), length(vertex, end), node);

                    if (nextNode.vertex.equals(end)) {
                        return Optional.of(backTrace(nextNode));
                    }

                    if (!visited.contains(nextNode)) {
                        priorityQueue.add(nextNode);
                    }
                }
            }
        }

        return Optional.empty();
    }

    private static <T, V extends Graph.Vertex<T>> List<V> backTrace(Node<T, V> node) {
        List<V> route = new LinkedList<>();
        while (node != null) {
            route.add(0, node.vertex);
            node = node.previous;
        }
        return route;
    }

    private static <T> double length(Graph.Vertex<T> v1, Graph.Vertex<T> v2) {
        return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
    }

}
