package com.github.davityle.csbasics.data.heap;

import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("unchecked")
public class BinaryHeap<T extends Comparable<T>> {

    private final Comparator<T> comparator;
    private T[] heap;
    private int size;

    public BinaryHeap() {
        this(2);
    }

    public BinaryHeap(int initialCapacity) {
        this(initialCapacity, Comparable::compareTo);
    }

    public BinaryHeap(Comparator<T> comparator) {
        this(2, comparator);
    }

    public BinaryHeap(int initialCapacity, Comparator<T> comparator) {
        this.heap = (T[]) new Comparable[initialCapacity];
        this.comparator = comparator;
    }

    public T peek() {
        return heap[0];
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T value) {
        int index = size, parentIndex;
        // bubble up
        while(index > 0 && comparator.compare(value, heap[(parentIndex = parentIndex(index))]) < 0) {
            heap[index] = heap[parentIndex];
            index = parentIndex;
        }
        heap[index] = value;
        // resize if needed
        if (++size == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
    }

    public T poll() {
        T value = heap[0];
        T tmp = heap[--size];

        int index = 0, childIndex;
        // bubble down
        while ((childIndex = lesserChildIndex(index)) != -1 && comparator.compare(heap[childIndex], tmp) < 0) {
            heap[index] = heap[childIndex];
            index = childIndex;
        }
        heap[index] = tmp;

        return value;
    }

    private int lesserChildIndex(int ind) {
        int fstChild = kthChildIndex(ind, 1);
        if(fstChild >= size)
            return -1;
        int sndChild = kthChildIndex(ind, 2);
        boolean second = sndChild < size && comparator.compare(heap[sndChild], heap[fstChild]) < 0;
        return second ? sndChild : fstChild;
    }

    private int parentIndex(int index) {
        return (index - 1)/2;
    }

    private int kthChildIndex(int index, int k) {
        return 2*index + k;
    }
}
