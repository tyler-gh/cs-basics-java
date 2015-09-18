package com.github.davityle.csbasics.algorithm.sort;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 *
 */
public class MergeSortTest {

    @Test
    public void testSort() {
        // for 100% line coverage
        MergeSort m = new MergeSort();

        Integer[] array = new Integer[10000];
        Random random = new Random();
        for(int i = 0; i < array.length; i++) {
            array[i] = random.nextInt();
        }

        MergeSort.sort(array);

        int previous = Integer.MIN_VALUE;
        for (Integer i : array) {
            assertTrue(previous <= i);
            previous = i;
        }
    }

}