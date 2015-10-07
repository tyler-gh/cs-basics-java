package com.github.davityle.csbasics.algorithm.graph;

import javax.swing.*;
import java.awt.*;

class GraphFrame extends JFrame {

    public static final int WIDTH = 700, HEIGHT = 700;

    final JPanel top;
    final JPanel bottom;
    final JLabel searchTime;

    GraphFrame(String windowTitle) {
        super(windowTitle);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        top = new JPanel();
        bottom = new JPanel();
        searchTime = new JLabel("");

        top.setLayout(new FlowLayout());
        bottom.setSize(WIDTH, HEIGHT);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(top);
        contentPane.add(bottom);
        contentPane.add(searchTime);

        pack();
        setResizable(false);
        setLocationByPlatform(true);
    }

    public JTextField textField(String val, String title)  {
        JTextField tf = new JTextField();
        tf.setText(val);
        Dimension size = new Dimension(45, 25);
        tf.setSize(size);
        tf.setPreferredSize(size);
        top.add(new Label(title));
        top.add(tf);
        return tf;
    }

}
