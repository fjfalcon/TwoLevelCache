package com.fjfalcon.strategies;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator<K> implements Comparator<K> {
    private Map<K, Long> map;

    public ValueComparator(Map<K, Long> map) {
        this.map = map;
    }

    @Override
    public int compare(K a, K b) {
        return map.get(a).compareTo(map.get(b));
    }

}
