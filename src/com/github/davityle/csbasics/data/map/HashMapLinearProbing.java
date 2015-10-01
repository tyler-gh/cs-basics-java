package com.github.davityle.csbasics.data.map;

public class HashMapLinearProbing<T, R> extends HashMap<T, R> {
    @Override
    protected int getIndex(T key) {
        int hash = key.hashCode();
        int index = modLength(hash);
        while (collision(index, hash, key)) {
            if (++index == table.length) {
                index = 0;
            }
        }
        return index;
    }
}
