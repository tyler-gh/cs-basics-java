package com.github.davityle.csbasics.data.list;

import java.util.OptionalInt;

public class LinkedList<T> implements Queue<T>, Stack<T>, List<T> {

    private int size;
    private final Node<T> headNode, tailNode;

    public LinkedList() {
        this.headNode = new Node<>(null);
        this.tailNode = new Node<>(null);
        tailNode.previous = headNode;
    }

    public void add(T value) {
        Node<T> tail = tailNode.previous;
        Node<T> newNode = new Node<>(value);

        tail.next = newNode;
        newNode.previous = tail;
        tailNode.previous = newNode;

        size++;
    }

    public T poll() {
        return remove(0);
    }

    public void push(T value) {
        add(value);
    }

    public T pop() {
        T value = tailNode.previous.value;
        tailNode.previous = tailNode.previous.previous;
        tailNode.previous.next = null;
        size--;
        return value;
    }

    public T get(int index) {
        Node<T> n = headNode.next;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return n.value;
    }

    public T remove(int index) {
        if(index == size - 1) {
            return pop();
        }
        Node<T> left = headNode, n = headNode.next;
        for (int i = 0; i < index; i++) {
            left = n;
            n = n.next;
        }
        left.next = n.next;
        left.next.previous = left;
        size--;
        return n.value;
    }

    public OptionalInt indexOf(T value) {
        if(value == null)
            throw new IllegalArgumentException("value must not be null");
        Node<T> n = headNode.next;
        int i = 0;
        while (n != null) {
            if (value.equals(n.value))
                return OptionalInt.of(i);
            i++;
            n = n.next;
        }
        return OptionalInt.empty();
    }

    public boolean has(T value) {
        return indexOf(value).isPresent();
    }

    public int getSize() {
        return size;
    }

    private static class Node<T> {
        private T value;

        public Node(T value) {
            this.value = value;
        }

        private Node<T> next;
        private Node<T> previous;
    }

}
