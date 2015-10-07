package com.github.davityle.csbasics.algorithm.sort;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class QuickSortTest {

    @Test
    public void testSort() throws Exception {
        // for 100% line coverage
        new QuickSort();

        Integer[] array = new Integer[100];
        for(int i = 0; i < array.length; i++) {
            array[i] = array.length - i - 1;
        }

        QuickSort.sort(array);

        for(int i = 0; i < array.length; i++) {
            assertEquals(Integer.valueOf(i), array[i]);
        }
    }
}