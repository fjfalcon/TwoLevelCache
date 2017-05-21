package com.fjfalcon.strategies;

/**
 * LFU Strategy - Least Frequently Used
 * Default strategy
 */

public class LFUStrategy<K> extends Strategy<K> {
    @Override
    public void call(K key) {
        long freq = 1;
        if (map.containsKey(key)) {
            freq = map.get(key) + 1;
        }
        map.put(key, freq);
    }
}