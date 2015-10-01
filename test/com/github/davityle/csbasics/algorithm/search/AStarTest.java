package com.github.davityle.csbasics.algorithm.search;

import com.github.davityle.csbasics.data.graph.Graph;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AStarTest {

    @Test
    public void testShortestRoute() throws Exception {

        Graph.AdjVertex<String> wanamatapua = new Graph.AdjVertex<>(0, 0, "Wanamatapua");
        Graph.AdjVertex<String> endCity = new Graph.AdjVertex<>(20000, 20000, "End City");

        BiConsumer<Graph.AdjVertex<String>, Graph.AdjVertex<String>> testPaths = (city1, city2) -> {
            connectCities(generateCities(city1, city2, 4000, 2, new Random(0)));

            Optional<List<Graph.AdjVertex<String>>> optPath = AStar.shortestRoute(city1, city2);

            assertTrue(optPath.isPresent());

            List<Graph.AdjVertex<String>> path = optPath.get();

            assertEquals(city1, path.get(0));
            assertEquals(city2, path.get(path.size() - 1));
        };

        testPaths.accept(wanamatapua, endCity);

        Graph.AdjVertex<String> pocatello = new Graph.AdjVertex<>(-200, -80000, "Pocatello");
        Graph.AdjVertex<String> sanFran = new Graph.AdjVertex<>(40000, -30000, "San Franscisco");

        testPaths.accept(pocatello, sanFran);
    }

    private static GeneratedGraph generateCities(Graph.AdjVertex<String> c1, Graph.AdjVertex<String> c2, int blockRadius, int citiesPerBlock, Random random) {

        final double xMax = Math.max(c1.x, c2.x);
        final double yMax = Math.max(c1.y, c2.y);

        final double xMin = Math.min(c1.x, c2.x);
        final double yMin = Math.min(c1.y, c2.y);

        final double xDiff = Math.abs(xMax - xMin);
        final double yDiff = Math.abs(yMax - yMin);

        final int xBlocks = (int) (xDiff/blockRadius);
        final int yBlocks = (int) (yDiff/blockRadius);

        final List<Graph.AdjVertex<String>>[][] cities = new List[xBlocks][yBlocks];
        for(int x = 0; x < xBlocks; x++) {
            for (int y = 0; y < yBlocks; y++) {
                cities[x][y] = new ArrayList<>();
            }
        }

        Consumer<Graph.AdjVertex<String>> addCity = vertex -> {
            int xIndex = (int) ((Math.abs(vertex.x - xMin) / xDiff) * (xBlocks - 1));
            int yIndex = (int) ((Math.abs(vertex.y - yMin) / yDiff) * (yBlocks - 1));
            cities[xIndex][yIndex].add(vertex);
        };

        NameGenerator gen = new NameGenerator();

        int numberOfCities = xBlocks * yBlocks * citiesPerBlock;

        for(int i = 0; i < numberOfCities; i++) {
            addCity.accept(new Graph.AdjVertex<>(xMin + random.nextDouble() * xDiff, yMin + random.nextDouble() * yDiff, gen.getName()));
        }

        addCity.accept(c1);
        addCity.accept(c2);

        return new GeneratedGraph(xMax, yMax, xMin, yMin, xDiff, yDiff, xBlocks, yBlocks, cities);
    }

    private static void connectCities(GeneratedGraph graph) {
        for(int x = 0; x < graph.xBlocks - 1; x++) {
            for (int y = 0; y < graph.yBlocks - 1; y++) {
                connectCities(graph.cities[x][y]);
                connectCities(graph.cities[x][y], graph.cities[x + 1][y + 1]);
                connectCities(graph.cities[x][y], graph.cities[x + 1][y]);
                connectCities(graph.cities[x][y], graph.cities[x][y + 1]);
            }
        }
    }

    private static void connectCities(List<Graph.AdjVertex<String>> cities) {
        for(int i = 0; i < cities.size(); i++) {
            for(int j = 0; j < cities.size(); j++) {
                if(i != j) {
                    cities.get(i).adjacent.add(cities.get(j));
                }
            }
        }
    }

    private static void connectCities(List<Graph.AdjVertex<String>> cities1, List<Graph.AdjVertex<String>> cities2) {
        for (Graph.AdjVertex<String> city1 : cities1) {
            for (Graph.AdjVertex<String> city2 : cities2) {
                city1.adjacent.add(city2);
                city2.adjacent.add(city1);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AStarTest::createAndShowGui);
    }

    private static void createAndShowGui() {
        final int WIDTH = 800, HEIGHT = 800;

        JFrame frame = new JFrame("GRAPH");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel top = new JPanel();
        JPanel bottom = new JPanel();

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

        JButton button = new JButton("Find Shortest Path");
        top.add(button);
        button.addActionListener(e -> {
            Function<JTextField, Double> dblVal = field -> Double.parseDouble(field.getText());
            Function<JTextField, Integer> intVal = field -> Integer.parseInt(field.getText());

            Graph.AdjVertex<String> startCity = new Graph.AdjVertex<>(dblVal.apply(xMin), dblVal.apply(yMin), "");
            Graph.AdjVertex<String> endCity = new Graph.AdjVertex<>(dblVal.apply(xMax), dblVal.apply(yMax), "");
            final GeneratedGraph genGraph = generateCities(startCity, endCity, intVal.apply(blockSize), intVal.apply(density), new Random(intVal.apply(seed)));
            connectCities(genGraph);
            final Optional<List<Graph.AdjVertex<String>>> optPath = AStar.shortestRoute(startCity, endCity);

            List<Graph.AdjVertex<String>> path = optPath.orElse(Collections.emptyList());

            GraphDrawing graph = new GraphDrawing(genGraph, path, WIDTH, HEIGHT);
            bottom.removeAll();
            bottom.add(graph);
            frame.invalidate();
            frame.pack();
        });

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(top);
        contentPane.add(bottom);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        button.doClick();
    }
}

class GeneratedGraph {
    final double xMax;
    final double yMax;

    final double xMin;
    final double yMin;

    final double xDiff;
    final double yDiff;

    final int xBlocks;
    final int yBlocks;

    final List<Graph.AdjVertex<String>>[][] cities;


    GeneratedGraph(double xMax, double yMax, double xMin, double yMin, double xDiff, double yDiff, int xBlocks, int yBlocks, List<Graph.AdjVertex<String>>[][] cities) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.xMin = xMin;
        this.yMin = yMin;
        this.xDiff = xDiff;
        this.yDiff = yDiff;
        this.xBlocks = xBlocks;
        this.yBlocks = yBlocks;
        this.cities = cities;
    }

    public Set<Graph.AdjVertex<String>> getCities() {
        Set<Graph.AdjVertex<String>> citySet = new HashSet<>();
        for(List<Graph.AdjVertex<String>>[] blocks : cities) {
            for(List<Graph.AdjVertex<String>> block : blocks) {
                citySet.addAll(block);
            }
        }
        return citySet;
    }
}


class GraphDrawing extends JPanel {

    private final Set<Graph.AdjVertex<String>> vertices;
    private final List<Graph.AdjVertex<String>> path;
    private final GeneratedGraph genGraph;
    private final int minWidth, minHeight, maxWidth, maxHeight;

    public GraphDrawing(GeneratedGraph genGraph, List<Graph.AdjVertex<String>> path, final int width, final int height) {
        setSize(width, height);

        this.minWidth = (int) (getWidth() * .025);
        this.minHeight = (int) (getHeight() * .025);
        this.maxWidth = (int) (getWidth() * .925);
        this.maxHeight = (int) (getHeight() * .925);

        this.genGraph = genGraph;
        this.vertices = genGraph.getCities();
        this.path = path;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        final Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.gray);
        for(int i = 0; i <= genGraph.xBlocks; i++) {
            int x = minWidth + i * (maxWidth/genGraph.xBlocks);
            g2.drawLine(x, minHeight, x, minHeight + maxHeight);
        }
        for(int i = 0; i <= genGraph.yBlocks; i++) {
            int y = minHeight + i * (maxHeight/genGraph.yBlocks);
            g2.drawLine(minWidth, y, minWidth + maxWidth, y);
        }

        final BiConsumer<Point, Point> drawLine = (p1, p2) -> g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        final Function<Graph.AdjVertex, Point> toPoint = v -> {
            int x = minWidth + (int) ((Math.abs(v.x - genGraph.xMax) / Math.abs(genGraph.xMax - genGraph.xMin)) * maxWidth);
            int y = minHeight + (int) ((Math.abs(v.y - genGraph.yMin) / Math.abs(genGraph.yMax - genGraph.yMin)) * maxHeight);
            return new Point(x, y);
        };

        g2.setColor(new Color(30, 80, 120));
        vertices.forEach(vertex -> vertex.adjacent.forEach(adj -> drawLine.accept(toPoint.apply(vertex), toPoint.apply(adj))));

        g2.setColor(Color.green);
        List<Point> line = path.stream().map(toPoint).collect(Collectors.toList());

        for(int i = 1; i < line.size(); i++) {
            drawLine.accept(line.get(i - 1), line.get(i));
        }

        g2.setColor(Color.black);
        vertices.stream().forEach(vertex -> {
            Point p = toPoint.apply(vertex);
            g2.fillOval(p.x - 2, p.y - 2, 4, 4);
            g2.drawString(vertex.value, p.x, p.y);
        });
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
        String demoVocals[] = { "a", "e", "i", "o", "u", "ei", "ai", "ou", "j",
                "ji", "y", "oi", "au", "oo" };

        String demoStartConsonants[] = { "b", "c", "d", "f", "g", "h", "k",
                "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "z",
                "ch", "bl", "br", "fl", "gl", "gr", "kl", "pr", "st", "sh",
                "th" };

        String demoEndConsonants[] = { "b", "d", "f", "g", "h", "k", "l", "m",
                "n", "p", "r", "s", "t", "v", "w", "z", "ch", "gh", "nn", "st",
                "sh", "th", "tt", "ss", "pf", "nt"};

        String nameInstructions[] = { "vd", "cvdvd", "cvd", "vdvd"};

        this.vocals.addAll(Arrays.asList(demoVocals));
        this.startConsonants.addAll(Arrays.asList(demoStartConsonants));
        this.endConsonants.addAll(Arrays.asList(demoEndConsonants));
        this.nameInstructions.addAll(Arrays.asList(nameInstructions));
    }

    /**
     *
     * The names will look like this
     * (v=vocal,c=startConsonsonant,d=endConsonants): vd, cvdvd, cvd, vdvd
     *
     * @param vocals
     * pass something like {"a","e","ou",..}
     * @param startConsonants
     * pass something like {"s","f","kl",..}
     * @param endConsonants
     * pass something like {"th","sh","f",..}
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
     * @param nameInstructions
     * Use only the following letters:
     * (v=vocal,c=startConsonsonant,d=endConsonants)! Pass something
     * like {"vd", "cvdvd", "cvd", "vdvd"}
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

        for (int i = 0; i < l; i++) { char x = nameInstructions.charAt(0); switch (x) { case 'v': name += getRandomElementFrom(vocals); break; case 'c': name += getRandomElementFrom(startConsonants); break; case 'd': name += getRandomElementFrom(endConsonants); break; } nameInstructions = nameInstructions.substring(1); } return name; } private String firstCharUppercase(String name) { return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1); } private String getRandomElementFrom(List<String> v) {
        return v.get(randomInt(0, v.size() - 1));
    }
}