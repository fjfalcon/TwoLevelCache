package com.fjfalcon.cache;

import com.fjfalcon.strategies.LRUStrategy;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LRUCacheTest {
    private TwoLevelCache<Integer, String> cache;

    @After
    public void tearDown() throws Exception {
        cache.clear();
    }

    @Test
    public void replace() throws Exception {
        cache = new TwoLevelCache<>(2, 2, new LRUStrategy<>());

        for (int i = 0; i < 4; i++) {
            cache.put(i, "value");
            assertTrue(cache.contains(i));
            cache.get(i);
            assertTrue(cache.contains(i));
        }

        cache.put(4, "value");

        assertFalse(cache.contains(0)); // Should be removed
        assertTrue(cache.contains(1));
        assertTrue(cache.contains(2));
        assertTrue(cache.contains(3));
        assertTrue(cache.contains(4));
    }
}
