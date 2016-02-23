package com.github.davityle.csbasics.data.tree;


import java.util.Optional;

public class BinaryTree<T extends Comparable<T>> {

    private Node<T> root;

    public void insert(T value) {
        if (root == null) {
            root = new Node<>(value);
        } else {
            Optional<Node<T>> node = Optional.of(root);
            do {
                node = lesserChild(node.get());
            } while (node.isPresent() && node.get().compareTo(value) > 0);
            Optional<Node<T>> left = node.get().left;
            node.get().left = Optional.of(new Node<>(value));
            node.get().left.get().left = left;
        }
    }

    private Optional<Node<T>> lesserChild(Node<T> node) {
        return node.left.map(left -> node.right.map(right -> left.compareTo(right.value) < 0 ? right : left).orElse(left));
    }

    public static final class Node<T extends Comparable<T>> implements Comparable<T> {
        private Optional<Node<T>> left, right;
        private final T value;

        public Node(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public Optional<Node<T>> getLeft() {
            return left;
        }

        public void setLeft(Node<T> left) {
            this.left = Optional.ofNullable(left);
        }

        public Optional<Node<T>> getRight() {
            return right;
        }

        public void setRight(Node<T> right) {
            this.right = Optional.ofNullable(right);
        }

        public boolean hasChildren() {
            return left != null || right != null;
        }

        @Override
        public int compareTo(T o) {
            return value.compareTo(o);
        }
    }

}
