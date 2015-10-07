package com.github.davityle.csbasics.data.graph;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface Graph<T, V extends Graph.Vertex<T>> {

    void addVertex(V vertex);
    void addVertices(Collection<V> vertices);
    Set<V> getVertices();
    void addEdge(V a, V b);
    void addEdges(V vertex, Collection<V> vertices);
    List<V> getAdjacentVertices(V vertex);

    class Vertex<T> {
        public final double x, y;
        public final T value;

        public Vertex(double x, double y, T value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Vertex))
                return false;
            Vertex vert = (Vertex) obj;
            return x == vert.x && y == vert.y;
        }

        @Override
        public int hashCode() {
            return (int) (x * 7 + y * 13);
        }

        @Override
        public String toString() {
            return String.format("(%.2f, %.2f) - %s", x, y, value);
        }
    }

    class Edge<T> {
        public final Graph.Vertex<T> a, b;

        public Edge(Graph.Vertex<T> a, Graph.Vertex<T> b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "{" + a + ":" + b + "}";
        }
    }
}
