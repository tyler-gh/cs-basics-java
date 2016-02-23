package com.github.davityle.csbasics.data.list;

import java.util.OptionalInt;

public interface List<T> {
    void add(T value);
    T get(int index);
    T remove(int index);
    OptionalInt indexOf(T value);
    boolean has(T value);
    int getSize();
}
