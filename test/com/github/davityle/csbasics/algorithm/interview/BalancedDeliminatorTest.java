package com.github.davityle.csbasics.algorithm.interview;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.*;

/**
 *
 */
public class BalancedDeliminatorTest {

    @Test
    public void testAreDeliminatorsBalanced() throws Exception {
        new BalancedDeliminator(); // for code coverage
        Function<String, Boolean> b = BalancedDeliminator::areDeliminatorsBalanced;

        assertTrue(b.apply("{}[]()"));
        assertTrue(b.apply("([{}])"));
        assertTrue(b.apply("([]{})"));


        assertFalse(b.apply("([)]"));
        assertFalse(b.apply("([]"));
        assertFalse(b.apply("[])"));
        assertFalse(b.apply("([})"));

    }
}