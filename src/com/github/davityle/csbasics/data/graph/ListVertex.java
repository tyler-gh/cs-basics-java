package com.github.davityle.csbasics.data.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListVertex<T> extends Graph.Vertex<T> {
    private final List<ListVertex<T>> adjacent = new ArrayList<>();

    public ListVertex(double x, double y, T value) {
        super(x, y, value);
    }

    void addAdjacentVertex(ListVertex<T> listVertex) {
        adjacent.add(listVertex);
    }

    void addAdjacentVertices(Collection<ListVertex<T>> adjVertices) {
        adjacent.addAll(adjVertices);
    }

    List<ListVertex<T>> getAdjacentVertices() {
        return adjacent;
    }
}
