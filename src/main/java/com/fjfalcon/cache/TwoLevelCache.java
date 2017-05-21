package com.fjfalcon.cache;

import com.fjfalcon.cache.filesystem.FileSystemCache;
import com.fjfalcon.cache.memory.MemoryCache;
import com.fjfalcon.strategies.LFUStrategy;
import com.fjfalcon.strategies.Strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class TwoLevelCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(TwoLevelCache.class);
    private final MemoryCache<K, V> firstLevel;
    private final FileSystemCache<K, V> secondLevel;
    private final Strategy<K> strategy;

    public TwoLevelCache(Integer memorySize, Integer fsSize) {
        this.firstLevel = new MemoryCache<>(memorySize);
        this.secondLevel = new FileSystemCache<>(fsSize);
        this.strategy = new LFUStrategy<>();
    }

    public TwoLevelCache(Integer memorySize, Integer fsSize, Strategy<K> strategy) {
        this.firstLevel = new MemoryCache<>(memorySize);
        this.secondLevel = new FileSystemCache<>(fsSize);
        this.strategy = strategy;
    }

    @Override
    public void put(K key, V value) {
        logger.debug(String.format("Putting key %s", key));
        if (!firstLevel.isFull() || firstLevel.contains(key)) {
            logger.debug(String.format("Put key %s to 1st level", key));
            firstLevel.put(key, value);
            if (secondLevel.contains(key))
                secondLevel.remove(key);
        } else if (!secondLevel.isFull() || secondLevel.contains(key)) {
            logger.debug(String.format("Put key %s to 2nd level", key));
            secondLevel.put(key, value);
        } else {
            replace(key, value);
        }
        if (strategy.contains(key)) {
            logger.debug(String.format("Put key %s to strategy storage", key));
            strategy.call(key);
        }
    }

    @Override
    public boolean contains(K key) {
        return firstLevel.contains(key) || secondLevel.contains(key);
    }

    @Override
    public V get(K key) {
        if (firstLevel.contains(key)) {
            strategy.call(key);
            firstLevel.get(key);
        }
        if (secondLevel.contains(key)) {
            strategy.call(key);
            secondLevel.get(key);
        }

        return null;
    }

    @Override
    public void remove(K key) {
        if (firstLevel.contains(key)) {
            logger.debug(String.format("Removing key %s from 1st level", key));
            firstLevel.remove(key);
        }
        if (secondLevel.contains(key)) {
            logger.debug(String.format("Removing key %s from 2nd level", key));
            secondLevel.remove(key);
        }
        strategy.remove(key);
    }

    @Override
    public void clear() {
        firstLevel.clear();
        secondLevel.clear();
        strategy.clear();
    }

    @Override
    public int size() {
        return firstLevel.size() + secondLevel.size();
    }

    @Override
    public boolean isFull() {
        return false;
    }

    public MemoryCache<K, V> getFirstLevel() {
        return firstLevel;
    }

    public FileSystemCache<K, V> getSecondLevel() {
        return secondLevel;
    }

    public Strategy<K> getStrategy() {
        return strategy;
    }

    private void replace(K key, V value) {
        K replacementKey = strategy.getReplacementKey();
        logger.debug(String.format("Replacement key is %s", replacementKey));
        if (firstLevel.contains(replacementKey)) {
            logger.debug(String.format("Replacing key %s with key %s at 1st Level", replacementKey, key));
            firstLevel.remove(replacementKey);
            firstLevel.put(key, value);
        } else if (secondLevel.contains(replacementKey)) {
            logger.debug(String.format("Replacing key %s with key %s at 2nd Level", replacementKey, key));
            secondLevel.remove(replacementKey);
            secondLevel.put(key, value);
        }
    }
}
