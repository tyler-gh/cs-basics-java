package com.github.davityle.csbasics.algorithm.interview;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ArrayRotationTest {

    @Test
    public void testRotateArray() throws Exception {
        new ArrayRotation(); // for line coverage

        Integer[] array = new Integer[5];
        for(int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        assertTrue(Arrays.deepEquals(new Integer[]{2, 3, 4, 0, 1}, ArrayRotation.rotateArray(array, 2)));
    }
}