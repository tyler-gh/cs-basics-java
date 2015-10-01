package com.github.davityle.csbasics.data.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    public static final class AdjVertex<T> {
        public final double x, y;
        public final T value;
        public final List<AdjVertex<T>> adjacent = new ArrayList<>();

        public AdjVertex(double x, double y, T value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || !(obj instanceof AdjVertex))
                return false;
            AdjVertex vert = (AdjVertex) obj;
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

}
