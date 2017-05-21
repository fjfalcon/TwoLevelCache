package com.fjfalcon.cache.filesystem;

import com.fjfalcon.cache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

public class FileSystemCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(FileSystemCache.class);
    private final String DIR = "cache";
    private final ConcurrentHashMap<K, String> map;
    private Integer maxSize;

    public FileSystemCache(Integer maxSize) {
        this.maxSize = maxSize;
        map = new ConcurrentHashMap<>();
        generateDir();
    }

    @Override
    public void put(K key, V value) {
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            File file = generateFile();
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(value);
            oos.flush();
            map.put(key, file.getName());
        } catch (IOException e) {
            logger.error("Error while writing an object to a file", e);
        } finally {
            close(oos);
            close(fos);
        }

    }

    @Override
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    @Override
    public V get(K key) {
        if (contains(key)) {
            File file = getFile(key);
            ObjectInputStream ois = null;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                return (V) ois.readObject();
            } catch (Exception e) {
                logger.error(String.format("Can't read a file. %s: %s", file.getName(), e));
            } finally {
                close(fis);
                close(ois);

            }
        }
        logger.debug(format("Object with key '%s' does not exist", key));
        return null;
    }

    @Override
    public void remove(K key) {
        File file = getFile(key);
        if (file.delete()) {
            logger.debug(String.format("File %s with key %s has been deleted", file.getName(), key));
        } else {
            logger.debug(String.format("Can't delete file %s", file.getName()));
        }
        map.remove(key);
    }

    @Override
    public void clear() {
        File dir = new File(DIR);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {// remove cache files
                if (file.delete()) {
                    logger.debug(format("File '%s' has been deleted", file));
                } else {
                    logger.error(format("Can't delete a file %s", file));
                }
            }
        }
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isFull() {
        return map.size() == maxSize;
    }

    private File generateFile() {
        String filename = UUID.randomUUID().toString();
        return new File(DIR + File.separator + filename);
    }

    private File getFile(K key) {
        String filename = map.get(key);
        return new File(DIR + File.separator + filename);
    }


    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException nothing) {
                //do nothing...
            }
        }
    }

    private void generateDir() {
        File dir = new File(DIR);
        if (!dir.exists()) {
            logger.info(String.format("Creating DIR: %s", DIR));
            if (dir.mkdir())
                logger.info(String.format("Directory %s created", DIR));
            else
                logger.error(String.format("Can't create DIR %s", DIR));
        }
    }
}
