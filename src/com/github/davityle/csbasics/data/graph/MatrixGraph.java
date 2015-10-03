package com.github.davityle.csbasics.data.graph;


import java.util.*;

@SuppressWarnings("unchecked")
public class MatrixGraph<T> implements Graph<T, Graph.Vertex<T>> {

    private HashMap<Vertex<T>, Integer> verticesIndexes;
    private boolean[][] adjencencyMatrix;
    private Vertex<T>[] vertices;
    private int size;

    public MatrixGraph() {
        this(2);
    }

    public MatrixGraph(int initialSize) {
        adjencencyMatrix = new boolean[initialSize][initialSize];
        vertices = new Vertex[initialSize];
        verticesIndexes = new HashMap<>();
    }

    public Graph.Vertex<T> getVertex(int index) {
        return vertices[index];
    }

    @Override
    public void addVertex(Vertex<T> vertex) {
        verticesIndexes.put(vertex, size);
        vertices[size++] = vertex;
        checkResize();
    }

    private void checkResize() {
        if(size >= adjencencyMatrix.length) {
            resize();
        }
    }

    private void resize() {
        boolean[][] tmp = adjencencyMatrix;
        int length = adjencencyMatrix.length * 2;
        adjencencyMatrix = new boolean[length][length];
        for(int i = 0; i < tmp.length; i++) {
            System.arraycopy(tmp[i], 0, adjencencyMatrix[i], 0, tmp.length);
        }
        Vertex<T>[] vertexTmp = vertices;
        vertices = new Vertex[length];
        System.arraycopy(vertexTmp, 0, vertices, 0, vertexTmp.length);
    }

    @Override
    public void addVertices(Collection<Vertex<T>> vertices) {
        vertices.forEach(this::addVertex);
    }

    @Override
    public Set<Vertex<T>> getVertices() {
        return verticesIndexes.keySet();
    }

    @Override
    public void addEdge(Vertex<T> a, Vertex<T> b) {
        Integer idA = verticesIndexes.get(a);
        Integer idB = verticesIndexes.get(b);
        if(idA == null || idB == null)
            throw new IllegalArgumentException("Vertices must be in graph to add an edge");
        adjencencyMatrix[idA][idB] = true;
        adjencencyMatrix[idB][idA] = true;
    }

    @Override
    public void addEdges(Vertex<T> vertex, Collection<Vertex<T>> vertices) {
        vertices.forEach(v -> addEdge(vertex, v));
    }

    @Override
    public List<Vertex<T>> getAdjacentVertices(Vertex<T> vertex) {
        Integer id = verticesIndexes.get(vertex);
        if(id == null)
            throw new IllegalArgumentException(String.format("Vertex %s was not found", vertex));
        List<Vertex<T>> vertices = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            if(adjencencyMatrix[id][i]) {
                vertices.add(this.vertices[i]);
            }
        }

        return vertices;
    }
}
