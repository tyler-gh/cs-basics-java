package com.github.davityle.csbasics.algorithm.sort;

import java.util.Random;

public class QuickSort {
    QuickSort() {
    }

    private static final Random rand = new Random();

    public static <T extends Comparable<T>> void sort(T[] array) {
        sort(array, 0, array.length - 1);
    }

    public static <T extends Comparable<T>> void sort(T[] array, int bottom, int top) {
        if (bottom < top) {
            int pivot = pivot(array, bottom, top);
            sort(array, bottom, pivot);
            sort(array, pivot + 1, top);
        }
    }

    private static <T extends Comparable<T>> int pivot(T[] array, int bottom, int top) {
        int pivot = bottom + rand.nextInt(top - bottom);
        int leftIndex = bottom - 1;
        int rightIndex = top + 1;
        T value = array[pivot];
        while (true) {
            do {
                leftIndex++;
            } while (leftIndex < rightIndex && array[leftIndex].compareTo(value) < 0);

            do {
                rightIndex--;
            } while (leftIndex < rightIndex && array[rightIndex].compareTo(value) > 0);

            if (leftIndex < rightIndex)
                swap(array, leftIndex, rightIndex);
            else
                break;
        }
        return pivot;
    }

    private static <T extends Comparable<T>> void swap(T[] array, int leftIndex, int rightIndex) {
        T tmp = array[leftIndex];
        array[leftIndex] = array[rightIndex];
        array[rightIndex] = tmp;
    }


}
