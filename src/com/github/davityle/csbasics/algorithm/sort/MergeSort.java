package com.github.davityle.csbasics.algorithm.sort;

/**
 *
 */
public class MergeSort {

    MergeSort() {
    }

    public static <T extends Comparable<T>> void sort(T[] array) {
        sort(array, 0, array.length - 1);
    }

    public static <T extends Comparable<T>> void sort(T[] array, int min, int max) {
        int mid = (max + min) / 2;
        if (max - min > 1) {
            sort(array, min, mid);
            sort(array, mid + 1, max);
        }

        int left = min, right = mid + 1;
        while (left <= mid && right <= max) {
            if (array[left].compareTo(array[right]) < 0) {
                left++;
            } else {
                T tmp = array[right];
                System.arraycopy(array, left, array, left + 1, right - left);
                array[left] = tmp;
                left++;
                mid++;
                right++;
            }
        }
    }


}
