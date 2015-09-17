package com.github.davityle.csbasics.data.list;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class ArrayList<T> implements List<T> {

    private T[] list;
    private int size;

    public ArrayList() {
        this(2);
    }

    public ArrayList(int initialCapacity) {
        this.list = (T[]) new Object[initialCapacity];
    }

    public void add(T value) {
        list[size] = value;
        if (++size == list.length) {
            list = Arrays.copyOf(list, list.length * 2);
        }
    }

    public T get(int index) {
        return list[index];
    }

    public T remove(int index) {
        T value = list[index];
        System.arraycopy(list, index + 1, list, index, size - (index + 1));
        size--;
        return value;
    }

    public Optional<Integer> indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if (list[i].equals(value)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public boolean has(T value) {
        return indexOf(value).isPresent();
    }

    public int getSize() {
        return size;
    }

}
