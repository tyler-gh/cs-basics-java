package com.github.davityle.csbasics.data.heap;


import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class BinaryHeapTest {

    @Test
    public void testBinaryHeap() throws Exception {
        BinaryHeap<Integer> heap = new BinaryHeap<>();

        Random random = new Random();
        for(int i = 0; i < 10000; i++) {
            heap.add(random.nextInt());
        }
        int previous = Integer.MIN_VALUE;
        for(int i = 0; i < 10000; i++) {
            assertTrue(previous <= heap.pop());
        }
    }

    @Test
    public void testPeek() throws Exception {
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.add(5);
        heap.add(4);
        heap.add(3);
        heap.add(2);
        heap.add(0);

        assertEquals(Integer.valueOf(0), heap.peek());
        heap.pop();
        assertEquals(Integer.valueOf(2), heap.peek());
        heap.pop();
        assertEquals(Integer.valueOf(3), heap.peek());
        heap.pop();
        assertEquals(Integer.valueOf(4), heap.peek());

    }

    @Test
    public void testGetSize() throws Exception {
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        assertEquals(0, heap.getSize());
        heap.add(5);
        heap.add(4);
        assertEquals(2, heap.getSize());
        heap.add(3);
        heap.add(2);
        assertEquals(4, heap.getSize());
        heap.add(0);
        assertEquals(5, heap.getSize());
        heap.pop();
        heap.pop();
        assertEquals(3, heap.getSize());
        heap.pop();
        heap.pop();
        assertEquals(1, heap.getSize());
    }
}