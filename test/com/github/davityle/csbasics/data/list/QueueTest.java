package com.github.davityle.csbasics.data.list;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class QueueTest {

    @Test
    public void testQueue() throws Exception {
        Queue<String> queue = new LinkedList<>();

        queue.add("1st");
        queue.add("2nd");
        queue.add("3rd");
        queue.add("4th");

        assertEquals("1st", queue.poll());
        assertEquals("2nd", queue.poll());
        assertEquals("3rd", queue.poll());

        queue.add("5th");
        queue.add("6th");

        assertEquals("4th", queue.poll());
        assertEquals("5th", queue.poll());
        assertEquals("6th", queue.poll());

        assertEquals(0, queue.getSize());
    }

}