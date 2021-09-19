package com.github.mimsic.base.ignite.provider;

import com.github.mimsic.base.common.provider.Provider;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.Query;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

public class IgniteProvider<K, V> implements Provider<K, V> {

    protected final Ignite ignite;
    protected final String qualifier;

    protected final CacheConfiguration<K, V> cacheConfiguration;

    public IgniteProvider(Ignite ignite, String qualifier, Supplier<CacheConfiguration<K, V>> configuration) {

        Optional.ofNullable(ignite).orElseThrow(() -> new IllegalArgumentException("Ignite is null"));
        Optional.ofNullable(qualifier).orElseThrow(() -> new IllegalArgumentException("Qualifier is null"));

        this.ignite = ignite;
        this.qualifier = qualifier;
        this.cacheConfiguration = configuration.get();
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
        Query<Cache.Entry<K, V>> query = new ScanQuery<>(null);
        try (QueryCursor<Cache.Entry<K, V>> cursor = cache().query(query)) {
            return asList(cursor);
        }
    }

    @Override
    public List<V> values(Class<V> clazz, String sql_query) {
        SqlQuery<K, V> query = new SqlQuery<>(clazz, sql_query);
        try (QueryCursor<Cache.Entry<K, V>> cursor = cache().query(query)) {
            return asList(cursor);
        }
    }

    @Override
    public Lock lock(K key) {
        return cache().lock(key);
    }

    @Override
    public void delete(K key) {
        cache().clear(key);
    }

    @Override
    public void deleteAll(List<K> keys) {
        cache().clearAll(new HashSet<>(keys));
    }

    @Override
    public void save(K key, V value) {
        cache().put(key, value);
    }

    @Override
    public void saveAll(Map<K, V> map) {
        cache().putAll(map);
    }

    public IgniteCache<K, V> cache() {
        return ignite.getOrCreateCache(cacheConfiguration);
    }

    private V onCacheMiss() {
        return null;
    }

    private List<V> asList(QueryCursor<Cache.Entry<K, V>> cursor) {
        List<V> results = new ArrayList<>(2000);
        cursor.forEach(entry -> results.add(entry.getValue()));
        return results;
    }
}
