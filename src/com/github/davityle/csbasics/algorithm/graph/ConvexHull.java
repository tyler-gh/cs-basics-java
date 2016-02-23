package com.github.davityle.csbasics.algorithm.graph;

import com.github.davityle.csbasics.data.graph.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class ConvexHull {

    public static <T> List<Graph.Edge<T>> outerMostPath(Graph<T, Graph.Vertex<T>> graph) {
        return outerMostPath(new ArrayList<>(graph.getVertices()));
    }

    public static <T> List<Graph.Edge<T>> outerMostPath(List<Graph.Vertex<T>> list) {
        if(list.size() == 0)
            return Collections.emptyList();

        Comparator<Graph.Vertex<T>> xComparator = (v1, v2) -> {
            if (v1.x > v2.x)
                return -1;
            if (v1.x < v2.x)
                return 1;
            return 0;
        };

        Comparator<Graph.Vertex<T>> yComparator = (v1, v2) -> {
            if (v1.y > v2.y)
                return -1;
            if (v1.y < v2.y)
                return 1;
            return 0;
        };

        Graph.Vertex<T> left = list.stream().min(xComparator).get();
        Graph.Vertex<T> right = list.stream().max(xComparator).get();

        Graph.Vertex<T> bottom = list.stream().min(yComparator).get();
        Graph.Vertex<T> top = list.stream().max(yComparator).get();

        List<Graph.Edge<T>> edges = new ArrayList<>();

        edges.addAll(quickHull(new Graph.Edge<>(left, top), list));
        edges.addAll(quickHull(new Graph.Edge<>(top, right), list));
        edges.addAll(quickHull(new Graph.Edge<>(right, bottom), list));
        edges.addAll(quickHull(new Graph.Edge<>(bottom, left), list));

        return edges;
    }

    private static <T> List<Graph.Edge<T>> quickHull(Graph.Edge<T> edge, List<Graph.Vertex<T>> points) {
        List<Graph.Vertex<T>> filtered = points.stream().filter(point -> distanceFromEdge(edge, point) > 0).collect(Collectors.toList());

        return filtered.stream()
                .min((p1, p2) -> {
                    double p1Distance = distanceFromEdge(edge, p1);
                    double p2Distance = distanceFromEdge(edge, p2);
                    if (p1Distance > p2Distance)
                        return -1;
                    if (p1Distance < p2Distance)
                        return 1;
                    return 0;
                })
                .map(p -> {
                    List<Graph.Edge<T>> edges = new ArrayList<>();
                    edges.addAll(quickHull(new Graph.Edge<>(edge.a, p), filtered));
                    edges.addAll(quickHull(new Graph.Edge<>(p, edge.b), filtered));
                    return edges;
                })
                .orElse(Collections.singletonList(edge));
    }

    private static <T> double distanceFromEdge(Graph.Edge<T> l, Graph.Vertex<T> c) {
        return (l.b.x - l.a.x) * (c.y - l.a.y) - (l.b.y - l.a.y) * (c.x - l.a.x);
    }

}
