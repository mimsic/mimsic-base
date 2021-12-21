package com.github.mimsic.base.persistence.model.provider.impl;

import com.github.mimsic.base.common.provider.Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class ModelProvider<K, V> implements Provider<K, V> {

    protected final Map<K, V> cache = new HashMap<>();
    protected final String qualifier;

    public ModelProvider(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public String qualifier() {
        return qualifier;
    }

    @Override
    public V value(K key) {
        V value = cache().get(key);
        if (value == null) {
            value = onCacheMiss();
        }
        return value;
    }

    @Override
    public List<V> values() {
        return new ArrayList<>(cache().values());
    }

    @Override
    public List<V> values(Class<V> clazz, String sql_query) {
        return null;
    }

    @Override
    public Lock lock(K key) {
        return null;
    }

    @Override
    public void delete(K key) {
        cache().remove(key);
    }

    @Override
    public void deleteAll(List<K> keys) {
        keys.forEach(key -> cache().remove(key));
    }

    @Override
    public void save(K key, V value) {
        cache().put(key, value);
    }

    @Override
    public void saveAll(Map<K, V> map) {
        cache().putAll(map);
    }

    public Map<K, V> cache() {
        return cache;
    }

    private V onCacheMiss() {
        return null;
    }
}
