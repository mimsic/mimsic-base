package com.github.mimsic.base.common.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.mimsic.base.common.json.ObjectMapperUtil;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

public class ObjectUtil {

    public ObjectUtil() {
    }

    public static <T> T castTo(Class<T> clazz, Object object) {
        return clazz.cast(object);
    }

    public static <K, V> Map<K, V> convertToMap(Object object) {

        return ObjectMapperUtil.convertValue(object, new TypeReference<Map<K, V>>() {
        });
    }

    public static <K, V> Map<K, V> convertToMap(Object object, TypeReference<Map<K, V>> typeRef) {

        return ObjectMapperUtil.convertValue(object, typeRef);
    }

    public static <K, V> V instance(Supplier<V> supplier, Map<K, V> map, Lock lock, K key) {

        V object = map.get(key);

        if (object == null) {
            lock.lock();
            try {
                object = map.get(key);
                if (object == null) {
                    object = supplier.get();
                    map.put(key, object);
                }
            } finally {
                lock.unlock();
            }
        }
        return object;
    }
}
