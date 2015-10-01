package com.github.davityle.csbasics.algorithm.interview;

public class ArrayRotation {

    ArrayRotation(){}

    public static <T> T[] rotateArray(T[] array, int count) {
        T[] result = (T[]) new Object[array.length];
        for(int i = 0; i < array.length; i++) {
            result[i] = array[(i + count) % array.length];
        }
        return result;
    }

}
