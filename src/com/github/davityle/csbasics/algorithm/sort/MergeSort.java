package com.github.davityle.csbasics.algorithm.sort;

public class MergeSort {

    MergeSort() {
    }

    public static <T extends Comparable<T>> void sort(T[] array) {
        sort(array, 0, array.length, (T[]) new Comparable[array.length]);
    }

    public static <T extends Comparable<T>> void sort(T[] array, int min, int max, T[] tmp) {

        if (max - min < 2)
            return;

        int mid = (max + min) / 2;

        sort(array, min, mid, tmp);
        sort(array, mid, max, tmp);

        int left = min, right = mid;
        for (int i = min; i < max; i++) {
            if (left < mid && (right >= max || array[left].compareTo(array[right]) <= 0)) {
                tmp[i] = array[left];
                left = left + 1;
            } else {
                tmp[i] = array[right];
                right = right + 1;
            }
        }

        System.arraycopy(tmp, min, array, min, max - min);
    }
}
