package com.github.davityle.csbasics.data.list;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class StackTest {

    @Test
    public void testStack() throws Exception {
        Stack<String> stack = new LinkedList<>();

        for(int i = 0; i <= ('z' - 'a'); i++) {
            stack.push(Character.toString((char) (i + 'a')));
        }

        assertEquals("z", stack.pop());
        assertEquals("y", stack.pop());
        assertEquals("x", stack.pop());
        assertEquals("w", stack.pop());
        assertEquals("v", stack.pop());

        assertEquals(('v' - 'a'), stack.getSize());

        stack.push("v");

        for(int i = ('v' - 'a'); i >= 0; i--) {
            assertEquals(Character.toString((char) (i + 'a')), stack.pop());
        }
    }
}