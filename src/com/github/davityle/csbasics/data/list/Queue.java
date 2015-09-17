package com.github.davityle.csbasics.data.list;

/**
 *
 */
public interface Queue<T> {
    void add(T value);
    T poll();
    int getSize();
}
