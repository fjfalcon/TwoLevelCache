package com.fjfalcon.cache;

import com.fjfalcon.strategies.LFUStrategy;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LFUCacheTest {
    private TwoLevelCache<Integer, String> cache;

    @After
    public void tearDown() throws Exception {
        cache.clear();
    }

    @Test
    public void replace() {
        cache = new TwoLevelCache<>(2,2,new LFUStrategy<>());

        cache.put(0, "value");
        cache.get(0);
        cache.get(0); // freq 2
        cache.put(1, "value");
        cache.get(1);
        cache.get(1); // freq 2
        cache.put(2, "value");
        cache.get(2); // freq 1
        cache.put(3, "value");
        cache.get(3);
        cache.get(3); // freq 2

        assertTrue(cache.contains(0));
        assertTrue(cache.contains(1));
        assertTrue(cache.contains(2));
        assertTrue(cache.contains(3));

        cache.put(4, "value");
        cache.get(4);
        cache.get(4); // freq 2

        assertTrue(cache.contains(0));
        assertTrue(cache.contains(1));
        assertFalse(cache.contains(2)); // Freq 1, should be removed
        assertTrue(cache.contains(3));
        assertTrue(cache.contains(4));
    }
}
