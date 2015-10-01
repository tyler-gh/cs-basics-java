package com.github.davityle.csbasics.algorithm.search;

import com.github.davityle.csbasics.data.graph.Graph;
import com.github.davityle.csbasics.data.heap.BinaryHeap;

import java.util.*;

public class AStar {

    private static final class Node<T> implements Comparable<Node<T>> {
        private final Graph.AdjVertex<T> vertex;
        private final double distance;
        private final double estimatedLength;
        private final Node<T> previous;

        private Node(Graph.AdjVertex<T> vertex, double distance, double heuristic, Node<T> previous) {
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
            if(obj == null || !(obj instanceof Node))
                return false;
            Node n = (Node) obj;
            return vertex.equals(n.vertex);
        }

        @Override
        public int compareTo(Node<T> n) {
            if(estimatedLength > n.estimatedLength)
                return 1;
            if(estimatedLength < n.estimatedLength)
                return -1;
            return 0;
        }

        @Override
        public String toString() {
            return String.format("%.4f - %s", estimatedLength, vertex);
        }
    }


    public static <T> Optional<List<Graph.AdjVertex<T>>> shortestRoute(Graph.AdjVertex<T> start, Graph.AdjVertex<T> end) {
        BinaryHeap<Node<T>> priorityQueue = new BinaryHeap<>();
        Set<Node<T>> visited = new HashSet<>();

        priorityQueue.add(new Node<>(start, 0, 0, null));
        while(!priorityQueue.isEmpty()) {
            final Node<T> node = priorityQueue.poll();
            if(visited.add(node)) {
                for (Graph.AdjVertex<T> vertex : node.vertex.adjacent) {

                    Node<T> nextNode = new Node<>(vertex, node.distance + length(node.vertex, vertex), length(vertex, end), node);

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

    private static <T> List<Graph.AdjVertex<T>> backTrace(Node<T> node) {
        List<Graph.AdjVertex<T>> route = new LinkedList<>();
        while(node != null) {
            route.add(0, node.vertex);
            node = node.previous;
        }
        return route;
    }

    private static <T> double length(Graph.AdjVertex<T> v1, Graph.AdjVertex<T> v2) {
        return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
    }

}
