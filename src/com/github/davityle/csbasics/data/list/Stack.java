package com.github.davityle.csbasics.data.list;

/**
 *
 */
public interface Stack<T> {
    void push(T value);
    T pop();
    int getSize();
}
