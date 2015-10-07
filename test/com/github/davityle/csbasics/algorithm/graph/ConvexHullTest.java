package com.github.davityle.csbasics.algorithm.graph;

import com.github.davityle.csbasics.data.graph.Graph;
import com.github.davityle.csbasics.data.graph.MatrixGraph;
import org.junit.Test;

import javax.swing.*;
import java.util.*;
import java.util.function.Function;

public class ConvexHullTest {

    @Test
    public void testOuterMostPath() throws Exception {

    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> {
            GraphFrame frame = new GraphFrame("Convex Hull");

            JTextField count = frame.textField("4000", "Count");
            JTextField seed = frame.textField("900", "Seed");

            JButton button = new JButton("Solve Convex Hull");
            frame.top.add(button);
            button.addActionListener(e -> {
                Function<JTextField, Integer> intVal = field -> Integer.parseInt(field.getText());

                Random random = new Random(intVal.apply(seed));
                NameGenerator nameGenerator = new NameGenerator();
                List<Graph.Vertex<String>> nodes = new ArrayList<>();

                for (int i = 0; i < intVal.apply(count); i++) {
                    nodes.add(new Graph.Vertex<>(random.nextGaussian(), random.nextGaussian(), nameGenerator.getName()));
                }

                MatrixGraph<String> graph = new MatrixGraph<>();
                graph.addVertices(nodes);

                long start = System.currentTimeMillis();
                List<Graph.Edge<String>> edges = ConvexHull.outerMostPath(graph);
                long end = System.currentTimeMillis();

                List<Graph.Vertex<String>> path = new ArrayList<>();

                edges.forEach(edge -> {
                    path.add(edge.a);
                    path.add(edge.b);
                });

                double xMin = path.stream().min((v1, v2) -> v1.x < v2.x ? -1 : v1.x > v2.x ? 1 : 0).map(v -> v.x).orElseGet(() -> 0d);
                double xMax = path.stream().min((v1, v2) -> v1.x < v2.x ? 1 : v1.x > v2.x ? -1 : 0).map(v -> v.x).orElseGet(() -> 0d);

                double yMin = path.stream().min((v1, v2) -> v1.y < v2.y ? -1 : v1.y > v2.y ? 1 : 0).map(v -> v.y).orElseGet(() -> 0d);
                double yMax = path.stream().min((v1, v2) -> v1.y < v2.y ? 1 : v1.y > v2.y ? -1 : 0).map(v -> v.y).orElseGet(() -> 0d);

                GeneratedGraph<Graph.Vertex<String>> gg = new GeneratedGraph<>(xMax, yMax, xMin, yMin, Math.abs(xMax - xMin), Math.abs(yMax - yMin), 1, 1, null, graph);
                GraphDrawing<? extends Graph.Vertex<String>> graphDrawing = new GraphDrawing<>(gg, path, GraphFrame.WIDTH, GraphFrame.HEIGHT);

                frame.bottom.removeAll();
                frame.bottom.add(graphDrawing);
                frame.searchTime.setText("Time: " + (end - start) + " ms");
                frame.invalidate();
                frame.pack();
            });

            button.doClick();
            frame.setVisible(true);
        });
    }
}