package com.github.mimsic.base.ignite.provider;

import org.apache.ignite.Ignite;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ExtendWith(SpringExtension.class) already exists in @SpringBootTest
 */
@SpringBootTest
public class IgniteCacheTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgniteCacheTest.class);

    private final String cacheName;
    private final String qualifier;
    private final String key;
    private final String value;

    @Autowired
    private Ignite ignite;

    public IgniteCacheTest() {

        this.cacheName = "TestCacheName";
        this.qualifier = "TestCacheQualifier";
        this.key = "key";
        this.value = "value";
    }

    @Test
    public void testCache() {

        IgniteProvider<String, String> igniteProvider = new IgniteProvider<>(ignite, qualifier, () -> {

            CacheConfiguration<String, String> cacheConfiguration = new CacheConfiguration<>();
            cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
            cacheConfiguration.setName(cacheName);
            cacheConfiguration.setBackups(1);
            return cacheConfiguration;
        });

        igniteProvider.save(key, value);
        Assertions.assertEquals(value, igniteProvider.value(key));
        LOGGER.error("Testing lock success");
    }
}
