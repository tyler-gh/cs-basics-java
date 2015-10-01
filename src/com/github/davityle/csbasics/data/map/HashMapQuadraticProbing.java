package com.github.davityle.csbasics.data.map;

public class HashMapQuadraticProbing<T, R> extends HashMap<T, R> {

    @Override
    protected int getIndex(T key) {
        int hash = key.hashCode();
        int index = modLength(hash);
        int count = 0;
        while (collision(index, hash, key)) {
            count++;
            index = (index + count * count) % table.length;
        }
        return index;
    }
}
