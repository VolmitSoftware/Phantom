package com.volmit.phantom.services;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.volmit.phantom.api.service.PhantomService;

import java.util.function.Supplier;

public class CacheService extends PhantomService {
    private final ConcurrentLinkedHashMap<String, Object> map =  new ConcurrentLinkedHashMap.Builder<String, Object>()
            .initialCapacity(1_000)
            .maximumWeightedCapacity(10_000)
            .concurrencyLevel(32)
            .build();

    public <T> T get(Object root, String key, Supplier<T> v)
    {
        return get(System.identityHashCode(root) + key, v);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Supplier<T> v)
    {
        return (T) map.computeIfAbsent(key, (k) -> v.get());
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
