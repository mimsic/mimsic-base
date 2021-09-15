package com.github.mimsic.base.common.provider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public interface Provider<K, V> {

    String qualifier();

    V value(K key);

    List<V> values();

    List<V> values(Class<V> clazz, String sql_query);

    Lock lock(K key);

    void delete(K key);

    void deleteAll(List<K> keys);

    void save(K key, V value);

    void saveAll(Map<K, V> map);
}
