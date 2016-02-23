package com.github.davityle.csbasics.data.tree;


import java.util.*;
import java.util.stream.Collectors;

public class TreeNode<T> {

    private final HashMap<T, TreeNode<T>> children = new HashMap<>();
    private final T value;
    private TreeNode<T> parent;


    public TreeNode() {
        this(null);
    }

    public TreeNode(T value) {
        this.value = value;
    }

    public Optional<TreeNode<T>> getNode(T child) {
        return Optional.ofNullable(children.get(child));
    }

    public TreeNode<T> getNodeOrThrow(T child) {
        return getNode(child).get();
    }

    public boolean has(T s) {
        return getNode(s).isPresent();
    }

    public TreeNode<T> add(T child) {
        return add(new TreeNode<>(child));
    }

    public TreeNode<T> add(TreeNode<T> child) {
        child.setParent(this);
        children.put(child.value, child);
        return child;
    }

    public void add(Collection<T> values) {
        values.forEach(this::add);
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public HashMap<T, TreeNode<T>> getChildren() {
        return children;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TreeNode))
            return false;
        TreeNode node = (TreeNode) obj;
        if (node.value == value)
            return true;
        if (node.value == null)
            return false;
        return node.value.equals(value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : super.hashCode();
    }

    public Iterator<T> breadthIterator() {
        return new BreadthIterator();
    }

    public Iterator<T> depthIterator() {
        return new DepthIterator();
    }

    private class BreadthIterator implements Iterator<T> {
        final Queue<TreeNode<T>> queue = new LinkedList<>();

        private BreadthIterator() {
            queue.addAll(TreeNode.this.children.values());
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public T next() {
            TreeNode<T> node = queue.poll();
            queue.addAll(node.children.values());
            return node.value;
        }
    }

    private class DepthIterator implements Iterator<T> {
        final Stack<TreeNode<T>> stack = new Stack<>();

        private DepthIterator() {
            addChildren(TreeNode.this);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            TreeNode<T> node = stack.pop();
            addChildren(node);
            return node.value;
        }

        private void addChildren(TreeNode<T> node) {
            node.children.values().stream()
                    .collect(Collectors.toCollection(LinkedList::new))
                    .descendingIterator()
                    .forEachRemaining(stack::push);
        }
    }
}
