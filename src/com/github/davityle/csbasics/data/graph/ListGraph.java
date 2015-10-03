package com.github.davityle.csbasics.data.graph;

import java.util.*;

public class ListGraph<T> implements Graph<T, ListVertex<T>> {

    Set<ListVertex<T>> vertices = new HashSet<>();

    @Override
    public void addVertex(ListVertex<T> vertex) {
        this.vertices.add(vertex);
    }

    @Override
    public void addVertices(Collection<ListVertex<T>> vertices) {
        this.vertices.addAll(vertices);
    }

    @Override
    public Set<ListVertex<T>> getVertices() {
        return vertices;
    }

    @Override
    public void addEdge(ListVertex<T> a, ListVertex<T> b) {
        a.addAdjacentVertex(b);
    }

    @Override
    public void addEdges(ListVertex<T> vertex, Collection<ListVertex<T>> vertices) {
        vertex.addAdjacentVertices(vertices);
    }

    @Override
    public List<ListVertex<T>> getAdjacentVertices(ListVertex<T> vertex) {
        return vertex.getAdjacentVertices();
    }


}
