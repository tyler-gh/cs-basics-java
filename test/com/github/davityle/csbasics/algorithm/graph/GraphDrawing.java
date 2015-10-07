package com.github.davityle.csbasics.algorithm.graph;

import com.github.davityle.csbasics.data.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
class GraphDrawing<V extends Graph.Vertex<String>> extends JPanel {

    private final Set<V> vertices;
    private final List<V> path;
    private final GeneratedGraph<V> genGraph;
    private final int minWidth, minHeight, maxWidth, maxHeight;

    public GraphDrawing(GeneratedGraph<V> genGraph, List<V> path, final int width, final int height) {
        setSize(width, height);

        this.minWidth = (int) (getWidth() * .025);
        this.minHeight = (int) (getHeight() * .025);
        this.maxWidth = (int) (getWidth() * .925);
        this.maxHeight = (int) (getHeight() * .925);

        this.genGraph = genGraph;
        this.vertices = genGraph.graph.getVertices();
        this.path = path;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        final Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.gray);
        for (int i = 0; i <= genGraph.xBlocks; i++) {
            int x = minWidth + i * (maxWidth / genGraph.xBlocks);
            g2.drawLine(x, minHeight, x, minHeight + maxHeight);
        }
        for (int i = 0; i <= genGraph.yBlocks; i++) {
            int y = minHeight + i * (maxHeight / genGraph.yBlocks);
            g2.drawLine(minWidth, y, minWidth + maxWidth, y);
        }

        final BiConsumer<Point, Point> drawLine = (p1, p2) -> g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        final Function<Graph.Vertex<String>, Point> toPoint = v -> {
            int x = minWidth + (int) ((Math.abs(v.x - genGraph.xMax) / Math.abs(genGraph.xMax - genGraph.xMin)) * maxWidth);
            int y = minHeight + (int) ((Math.abs(v.y - genGraph.yMin) / Math.abs(genGraph.yMax - genGraph.yMin)) * maxHeight);
            return new Point(x, y);
        };

        g2.setColor(new Color(30, 80, 120));
        vertices.forEach(vertex -> genGraph.graph.getAdjacentVertices(vertex).forEach(adj -> drawLine.accept(toPoint.apply(vertex), toPoint.apply(adj))));

        g2.setColor(Color.black);
        vertices.stream().forEach(vertex -> {
            Point p = toPoint.apply(vertex);
            g2.fillOval(p.x - 2, p.y - 2, 4, 4);
            g2.drawString(vertex.value, p.x, p.y);
        });

        g2.setColor(Color.green);
        List<Point> line = path.stream().map(toPoint).collect(Collectors.toList());

        for (int i = 1; i < line.size(); i++) {
            drawLine.accept(line.get(i - 1), line.get(i));
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
}
