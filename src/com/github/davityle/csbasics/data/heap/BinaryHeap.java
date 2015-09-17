package com.github.davityle.csbasics.data.heap;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class BinaryHeap<T extends Comparable<T>> {

    private T[] heap;
    private int size;

    public BinaryHeap() {
        this(2);
    }

    public BinaryHeap(int initialCapacity) {
        heap = (T[]) new Comparable[initialCapacity];
    }

    public T peek() {
        return heap[0];
    }

    public int getSize() {
        return size;
    }

    public void add(T value) {
        int index = size, parentIndex;
        // bubble up
        while(index > 0 && value.compareTo(heap[(parentIndex = parentIndex(index))]) < 0) {
            heap[index] = heap[parentIndex];
            index = parentIndex;
        }
        heap[index] = value;
        // resize if needed
        if (++size == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
    }

    public T pop() {
        T value = heap[0];
        T tmp = heap[--size];

        int index = 0, childIndex;
        // bubble down
        while ((childIndex = lesserChildIndex(index)) != -1 && heap[childIndex].compareTo(tmp) < 0) {
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
        boolean second = sndChild < size && heap[sndChild].compareTo(heap[fstChild]) < 0;
        return second ? sndChild : fstChild;
    }

    private int parentIndex(int index) {
        return (index - 1)/2;
    }

    private int kthChildIndex(int index, int k) {
        return 2*index + k;
    }
}
