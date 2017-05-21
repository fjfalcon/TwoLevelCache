package com.fjfalcon.strategies;

/**
 * LRU Strategy - Least Recently Used
 */

public class LRUStrategy<K> extends Strategy<K> {
    @Override
    public void call(K key) {
        map.put(key, System.nanoTime());
    }
}