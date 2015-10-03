package com.github.davityle.csbasics.algorithm.search;

import com.github.davityle.csbasics.data.graph.ListGraph;
import com.github.davityle.csbasics.data.graph.Graph;
import com.github.davityle.csbasics.data.graph.ListVertex;
import com.github.davityle.csbasics.data.graph.MatrixGraph;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

public class AStarTest {

    @Test
    public void testShortestRoute() throws Exception {

        Graph.Vertex<String> wanamatapua = new Graph.Vertex<>(0, 0, "Wanamatapua");
        Graph.Vertex<String> endCity = new Graph.Vertex<>(20000, 20000, "End City");

        BiConsumer<Graph.Vertex<String>, Graph.Vertex<String>> testPaths = (city1, city2) -> {
            GeneratedGraph<Graph.Vertex<String>> graph = connectCities(generateCities(city1, city2, 4000, 2, new Random(0), () -> new MatrixGraph<>(), (x, y) -> (val) -> new Graph.Vertex<>(x, y, val)));

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

    private static <V extends Graph.Vertex<String>> GeneratedGraph<V> generateCities(V c1, V c2, int blockRadius, int citiesPerBlock, Random random, Supplier<Graph<String, V>> graphSupplier, BiFunction<Double, Double, Function<String, V>> vertexSupplier) {

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

        return new GeneratedGraph<>(xMax, yMax, xMin, yMin, xDiff, yDiff, xBlocks, yBlocks, cities, graphSupplier);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AStarTest::createAndShowGui);
    }

    private static void createAndShowGui() {
        final int WIDTH = 700, HEIGHT = 700;

        JFrame frame = new JFrame("GRAPH");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel top = new JPanel();
        JPanel bottom = new JPanel();
        JLabel searchTime = new JLabel("");

        top.setLayout(new FlowLayout());
        bottom.setSize(WIDTH, HEIGHT);

        BiFunction<String, String, JTextField> textField = (val, text) -> {
            JTextField tf = new JTextField();
            tf.setText(val);
            Dimension size = new Dimension(45, 25);
            tf.setSize(size);
            tf.setPreferredSize(size);
            top.add(new Label(text));
            top.add(tf);
            return tf;
        };

        JTextField xMin = textField.apply("-9", "X-Min");
        JTextField xMax = textField.apply("9", "X-Max");
        JTextField yMin = textField.apply("-9", "Y-Min");
        JTextField yMax = textField.apply("9", "Y-Max");
        JTextField blockSize = textField.apply("3", "Block Size");
        JTextField density = textField.apply("3", "Density");
        JTextField seed = textField.apply("0", "Seed");

        JCheckBox checkBox = new JCheckBox("Matrix");
        top.add(checkBox);

        JButton button = new JButton("Find Shortest Path");
        top.add(button);
        button.addActionListener(e -> {
            Function<JTextField, Double> dblVal = field -> Double.parseDouble(field.getText());
            Function<JTextField, Integer> intVal = field -> Integer.parseInt(field.getText());
            Supplier<GraphDrawing<? extends Graph.Vertex<String>>> adjListSupplier = () -> {
                ListVertex<String> startCity = new ListVertex<>(dblVal.apply(xMin), dblVal.apply(yMin), "");
                ListVertex<String> endCity = new ListVertex<>(dblVal.apply(xMax), dblVal.apply(yMax), "");
                final GeneratedGraph<ListVertex<String>> genGraph = generateCities(startCity, endCity, intVal.apply(blockSize), intVal.apply(density), new Random(intVal.apply(seed)), () -> new ListGraph<>(), (x, y) -> (val) -> new ListVertex<>(x, y, val));
                connectCities(genGraph);
                final Optional<List<ListVertex<String>>> optPath = AStar.shortestRoute(startCity, endCity, genGraph.graph);

                List<ListVertex<String>> path = optPath.orElse(Collections.emptyList());

                return new GraphDrawing<>(genGraph, path, WIDTH, HEIGHT);
            };
            Supplier<GraphDrawing<? extends Graph.Vertex<String>>> adjMatrixSupplier = () -> {
                Graph.Vertex<String> city1 = new Graph.Vertex<>(dblVal.apply(xMin), dblVal.apply(yMin), "");
                Graph.Vertex<String> city2 = new Graph.Vertex<>(dblVal.apply(xMax), dblVal.apply(yMax), "");
                GeneratedGraph<Graph.Vertex<String>> genGraph = connectCities(generateCities(city1, city2, intVal.apply(blockSize), intVal.apply(density), new Random(intVal.apply(seed)), () -> new MatrixGraph<>(), (x, y) -> (val) -> new Graph.Vertex<>(x, y, val)));

                Optional<List<Graph.Vertex<String>>> optPath = AStar.shortestRoute(city1, city2, genGraph.graph);

                List<Graph.Vertex<String>> path = optPath.orElse(Collections.emptyList());

                return new GraphDrawing<>(genGraph, path, WIDTH, HEIGHT);
            };

            long start = System.currentTimeMillis();
            GraphDrawing<? extends Graph.Vertex<String>> graph = checkBox.isSelected() ? adjMatrixSupplier.get() : adjListSupplier.get();
            long end = System.currentTimeMillis();

            bottom.removeAll();
            bottom.add(graph);
            searchTime.setText("Time: " + (end - start) + " ms");
            frame.invalidate();
            frame.pack();
        });

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(top);
        contentPane.add(bottom);
        contentPane.add(searchTime);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        button.doClick();
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


    GeneratedGraph(double xMax, double yMax, double xMin, double yMin, double xDiff, double yDiff, int xBlocks, int yBlocks, List<V>[][] cities, Supplier<Graph<String, V>> graphSupplier) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.xMin = xMin;
        this.yMin = yMin;
        this.xDiff = xDiff;
        this.yDiff = yDiff;
        this.xBlocks = xBlocks;
        this.yBlocks = yBlocks;
        this.cities = cities;
        this.graph = graphSupplier.get();
        for (List<V>[] blocks : cities) {
            for (List<V> block : blocks) {
                graph.addVertices(block);
            }
        }
    }
}


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

// taken from http://andrdev.blogspot.com/2011/02/simple-random-name-generator.html
class NameGenerator {

    private List<String> vocals = new ArrayList<>();
    private List<String> startConsonants = new ArrayList<>();
    private List<String> endConsonants = new ArrayList<>();
    private List<String> nameInstructions = new ArrayList<>();

    public NameGenerator() {
        String demoVocals[] = {"a", "e", "i", "o", "u", "ei", "ai", "ou", "j",
                "ji", "y", "oi", "au", "oo"};

        String demoStartConsonants[] = {"b", "c", "d", "f", "g", "h", "k",
                "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "z",
                "ch", "bl", "br", "fl", "gl", "gr", "kl", "pr", "st", "sh",
                "th"};

        String demoEndConsonants[] = {"b", "d", "f", "g", "h", "k", "l", "m",
                "n", "p", "r", "s", "t", "v", "w", "z", "ch", "gh", "nn", "st",
                "sh", "th", "tt", "ss", "pf", "nt"};

        String nameInstructions[] = {"vd", "cvdvd", "cvd", "vdvd"};

        this.vocals.addAll(Arrays.asList(demoVocals));
        this.startConsonants.addAll(Arrays.asList(demoStartConsonants));
        this.endConsonants.addAll(Arrays.asList(demoEndConsonants));
        this.nameInstructions.addAll(Arrays.asList(nameInstructions));
    }

    /**
     * The names will look like this
     * (v=vocal,c=startConsonsonant,d=endConsonants): vd, cvdvd, cvd, vdvd
     *
     * @param vocals          pass something like {"a","e","ou",..}
     * @param startConsonants pass something like {"s","f","kl",..}
     * @param endConsonants   pass something like {"th","sh","f",..}
     */
    public NameGenerator(String[] vocals, String[] startConsonants,
                         String[] endConsonants) {
        this.vocals.addAll(Arrays.asList(vocals));
        this.startConsonants.addAll(Arrays.asList(startConsonants));
        this.endConsonants.addAll(Arrays.asList(endConsonants));
    }

    /**
     * see {@link NameGenerator#NameGenerator(String[], String[], String[])}
     *
     * @param vocals
     * @param startConsonants
     * @param endConsonants
     * @param nameInstructions Use only the following letters:
     *                         (v=vocal,c=startConsonsonant,d=endConsonants)! Pass something
     *                         like {"vd", "cvdvd", "cvd", "vdvd"}
     */
    public NameGenerator(String[] vocals, String[] startConsonants,
                         String[] endConsonants, String[] nameInstructions) {
        this(vocals, startConsonants, endConsonants);
        this.nameInstructions.addAll(Arrays.asList(nameInstructions));
    }

    public String getName() {
        return firstCharUppercase(getNameByInstructions(getRandomElementFrom(nameInstructions)));
    }

    private int randomInt(int min, int max) {
        return (int) (min + (Math.random() * (max + 1 - min)));
    }

    private String getNameByInstructions(String nameInstructions) {
        String name = "";
        int l = nameInstructions.length();

        for (int i = 0; i < l; i++) {
            char x = nameInstructions.charAt(0);
            switch (x) {
                case 'v':
                    name += getRandomElementFrom(vocals);
                    break;
                case 'c':
                    name += getRandomElementFrom(startConsonants);
                    break;
                case 'd':
                    name += getRandomElementFrom(endConsonants);
                    break;
            }
            nameInstructions = nameInstructions.substring(1);
        }
        return name;
    }

    private String firstCharUppercase(String name) {
        return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1);
    }

    private String getRandomElementFrom(List<String> v) {
        return v.get(randomInt(0, v.size() - 1));
    }
}