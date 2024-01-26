package com.productiontracking.cache;

public interface CacheManagerService {
    <T> T get(String key, Class<T> type);

    <T> void add(String key, T item);

    void remove(String key);

    boolean contains(String key);

}
