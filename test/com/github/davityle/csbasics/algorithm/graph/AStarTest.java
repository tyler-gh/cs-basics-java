package com.github.davityle.csbasics.algorithm.graph;

import com.github.davityle.csbasics.data.graph.ListGraph;
import com.github.davityle.csbasics.data.graph.Graph;
import com.github.davityle.csbasics.data.graph.ListVertex;
import com.github.davityle.csbasics.data.graph.MatrixGraph;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

import java.util.*;
import java.util.List;
import java.util.function.*;

public class AStarTest {

    @Test
    public void testShortestRoute() throws Exception {

        Graph.Vertex<String> wanamatapua = new Graph.Vertex<>(0, 0, "Wanamatapua");
        Graph.Vertex<String> endCity = new Graph.Vertex<>(20000, 20000, "End City");

        BiConsumer<Graph.Vertex<String>, Graph.Vertex<String>> testPaths = (city1, city2) -> {
            GeneratedGraph<Graph.Vertex<String>> graph = connectCities(generateCities(city1, city2, 4000, 2, new Random(0), new MatrixGraph<>(), (x, y) -> (val) -> new Graph.Vertex<>(x, y, val)));

            Optional<List<Graph.Vertex<String>>> optPath = AStar.shortestRoute(city1, city2, graph.graph);

            assertTrue(optPath.isPresent());

            List<Graph.Vertex<String>> path = optPath.get();

            assertEquals(city1, path.get(0));
            assertEquals(city2, path.get(path.size() - 1));
        };

        testPaths.accept(wanamatapua, endCity);

        Graph.Vertex<String> pocatello = new Graph.Vertex<>(-200, -80000, "Pocatello");
        Graph.Vertex<String> sanFran = new Graph.Vertex<>(40000, -30000, "San Franscisco");

        testPaths.accept(pocatello, sanFran);


        MatrixGraph<UUID> matrixGraph = new MatrixGraph<>();
        matrixGraph.addVertex(new Graph.Vertex<>(0, 0, UUID.randomUUID()));
        matrixGraph.addVertex(new Graph.Vertex<>(1, 1, UUID.randomUUID()));
        matrixGraph.addVertex(new Graph.Vertex<>(-1, -1, UUID.randomUUID()));
        matrixGraph.addVertex(new Graph.Vertex<>(-1, 1, UUID.randomUUID()));

        matrixGraph.addEdge(matrixGraph.getVertex(0), matrixGraph.getVertex(1));
        matrixGraph.addEdge(matrixGraph.getVertex(1), matrixGraph.getVertex(2));
        matrixGraph.addEdge(matrixGraph.getVertex(0), matrixGraph.getVertex(2));

        Optional<List<Graph.Vertex<UUID>>> optPath = AStar.shortestRoute(matrixGraph.getVertex(0), matrixGraph.getVertex(3), matrixGraph);
        assertEquals(Optional.empty(), optPath);
    }

    private static <V extends Graph.Vertex<String>> GeneratedGraph<V> generateCities(V c1, V c2, int blockRadius, int citiesPerBlock, Random random, Graph<String, V> graph, BiFunction<Double, Double, Function<String, V>> vertexSupplier) {

        final double xMax = Math.max(c1.x, c2.x);
        final double yMax = Math.max(c1.y, c2.y);

        final double xMin = Math.min(c1.x, c2.x);
        final double yMin = Math.min(c1.y, c2.y);

        final double xDiff = Math.abs(xMax - xMin);
        final double yDiff = Math.abs(yMax - yMin);

        final int xBlocks = (int) (xDiff / blockRadius);
        final int yBlocks = (int) (yDiff / blockRadius);

        final List<V>[][] cities = new List[xBlocks][yBlocks];
        for (int x = 0; x < xBlocks; x++) {
            for (int y = 0; y < yBlocks; y++) {
                cities[x][y] = new ArrayList<>();
            }
        }

        Consumer<V> addCity = (V vertex) -> {
            int xIndex = (int) ((Math.abs(vertex.x - xMin) / xDiff) * (xBlocks - 1));
            int yIndex = (int) ((Math.abs(vertex.y - yMin) / yDiff) * (yBlocks - 1));
            cities[xIndex][yIndex].add(vertex);
        };

        NameGenerator gen = new NameGenerator();

        int numberOfCities = xBlocks * yBlocks * citiesPerBlock;

        for (int i = 0; i < numberOfCities; i++) {
            addCity.accept(vertexSupplier.apply(xMin + random.nextDouble() * xDiff, yMin + random.nextDouble() * yDiff).apply(gen.getName()));
        }

        addCity.accept(c1);
        addCity.accept(c2);

        return new GeneratedGraph<>(xMax, yMax, xMin, yMin, xDiff, yDiff, xBlocks, yBlocks, cities, graph);
    }

    private static <V extends Graph.Vertex<String>> GeneratedGraph<V> connectCities(GeneratedGraph<V> graph) {
        for (int x = 0; x < graph.xBlocks - 1; x++) {
            for (int y = 0; y < graph.yBlocks - 1; y++) {
                connectCities(graph.cities[x][y], graph.graph);
                connectCities(graph.cities[x][y], graph.cities[x + 1][y + 1], graph.graph);
                connectCities(graph.cities[x][y], graph.cities[x + 1][y], graph.graph);
                connectCities(graph.cities[x][y], graph.cities[x][y + 1], graph.graph);
            }
        }
        return graph;
    }

    private static <V extends Graph.Vertex<String>> void connectCities(List<V> cities, Graph<String, V> graph) {
        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                if (i != j) {
                    graph.addEdge(cities.get(i), cities.get(j));
                }
            }
        }
    }

    private static <V extends Graph.Vertex<String>> void connectCities(List<V> cities1, List<V> cities2, Graph<String, V> graph) {
        for (V city1 : cities1) {
            for (V city2 : cities2) {
                graph.addEdge(city1, city2);
                graph.addEdge(city2, city1);
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> {
            GraphFrame frame = new GraphFrame("A*");

            JTextField xMin = frame.addTextField("-9", "X-Min");
            JTextField xMax = frame.addTextField("9", "X-Max");
            JTextField yMin = frame.addTextField("-9", "Y-Min");
            JTextField yMax = frame.addTextField("9", "Y-Max");
            JTextField blockSize = frame.addTextField("3", "Block Size");
            JTextField density = frame.addTextField("3", "Density");
            JTextField seed = frame.addTextField("0", "Seed");

            JCheckBox checkBox = new JCheckBox("Matrix");
            frame.top.add(checkBox);

            JButton button = new JButton("Find Shortest Path");
            frame.top.add(button);
            button.addActionListener(e -> {
                Function<JTextField, Double> dblVal = field -> Double.parseDouble(field.getText());
                Function<JTextField, Integer> intVal = field -> Integer.parseInt(field.getText());
                Supplier<GraphDrawing<? extends Graph.Vertex<String>>> adjListSupplier = () -> {
                    ListVertex<String> startCity = new ListVertex<>(dblVal.apply(xMin), dblVal.apply(yMin), "");
                    ListVertex<String> endCity = new ListVertex<>(dblVal.apply(xMax), dblVal.apply(yMax), "");

                    GeneratedGraph<ListVertex<String>> genGraph = connectCities(generateCities(startCity, endCity, intVal.apply(blockSize), intVal.apply(density), new Random(intVal.apply(seed)), new ListGraph<>(), (x, y) -> (val) -> new ListVertex<>(x, y, val)));
                    List<ListVertex<String>> path = AStar.shortestRoute(startCity, endCity, genGraph.graph).orElse(Collections.emptyList());

                    return new GraphDrawing<>(genGraph, path, GraphFrame.WIDTH, GraphFrame.HEIGHT);
                };
                Supplier<GraphDrawing<? extends Graph.Vertex<String>>> adjMatrixSupplier = () -> {
                    Graph.Vertex<String> city1 = new Graph.Vertex<>(dblVal.apply(xMin), dblVal.apply(yMin), "");
                    Graph.Vertex<String> city2 = new Graph.Vertex<>(dblVal.apply(xMax), dblVal.apply(yMax), "");

                    GeneratedGraph<Graph.Vertex<String>> genGraph = connectCities(generateCities(city1, city2, intVal.apply(blockSize), intVal.apply(density), new Random(intVal.apply(seed)), new MatrixGraph<>(), (x, y) -> (val) -> new Graph.Vertex<>(x, y, val)));

                    List<Graph.Vertex<String>> path = AStar.shortestRoute(city1, city2, genGraph.graph).orElse(Collections.emptyList());

                    return new GraphDrawing<>(genGraph, path, GraphFrame.WIDTH, GraphFrame.HEIGHT);
                };

                long start = System.currentTimeMillis();
                GraphDrawing<? extends Graph.Vertex<String>> graph = checkBox.isSelected() ? adjMatrixSupplier.get() : adjListSupplier.get();
                long end = System.currentTimeMillis();

                frame.bottom.removeAll();
                frame.bottom.add(graph);
                frame.searchTime.setText("Time: " + (end - start) + " ms");
                frame.invalidate();
                frame.pack();
            });

            button.doClick();
            frame.setVisible(true);
        });
    }
}

class GeneratedGraph<V extends Graph.Vertex<String>> {
    final double xMax;
    final double yMax;

    final double xMin;
    final double yMin;

    final double xDiff;
    final double yDiff;

    final int xBlocks;
    final int yBlocks;

    final List<V>[][] cities;

    final Graph<String, V> graph;


    GeneratedGraph(double xMax, double yMax, double xMin, double yMin, double xDiff, double yDiff, int xBlocks, int yBlocks, List<V>[][] cities, Graph<String, V> graph) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.xMin = xMin;
        this.yMin = yMin;
        this.xDiff = xDiff;
        this.yDiff = yDiff;
        this.xBlocks = xBlocks;
        this.yBlocks = yBlocks;
        this.cities = cities;
        this.graph = graph;
        if(cities != null) {
            for (List<V>[] blocks : cities) {
                for (List<V> block : blocks) {
                    graph.addVertices(block);
                }
            }
        }
    }
}


