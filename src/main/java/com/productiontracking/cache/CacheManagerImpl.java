package com.productiontracking.cache;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Caffeine;

@Component
public class CacheManagerImpl implements CacheManagerService {

    private final CacheManager cacheManager;

    public CacheManagerImpl() {
        cacheManager = initCacheManager();
    }

    private CacheManager initCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeineBuilder());
        cacheManager.setCacheNames(Arrays.asList("default"));
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        Cache cache = cacheManager.getCache("default");
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                return type.cast(valueWrapper.get());
            }
        }
        return null;
    }

    @Override
    public <T> void add(String key, T item) {
        Cache cache = cacheManager.getCache("default");
        if (cache != null) {
            cache.put(key, item);
        }
    }

    @Override
    public void remove(String key) {
        Cache cache = cacheManager.getCache("default");
        if (cache != null) {
            cache.evict(key);
        }
    }

    @Override
    public boolean contains(String key) {
        Cache cache = cacheManager.getCache("default");
        return cache != null && cache.get(key) != null;
    }

}