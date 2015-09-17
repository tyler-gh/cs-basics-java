package com.github.davityle.csbasics.algorithm.sort;

import org.junit.Test;

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
        for(int i = 0; i < array.length; i++) {
            array[i] = array.length - i - 1;
        }

        MergeSort.sort(array);

        for(int i = 0; i < array.length; i++) {
            assertEquals(Integer.valueOf(i), array[i]);
        }

    }

}