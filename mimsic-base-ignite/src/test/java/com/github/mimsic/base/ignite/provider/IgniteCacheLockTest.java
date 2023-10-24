package com.github.mimsic.base.ignite.provider;

import org.apache.ignite.Ignite;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;

/**
 * @ExtendWith(SpringExtension.class) already exists in @SpringBootTest
 */
@Disabled
@SpringBootTest
public class IgniteCacheLockTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgniteCacheLockTest.class);

    private final String cacheName;
    private final String qualifier;
    private final String key;

    private IgniteProvider<String, String> igniteProvider;
    private CountDownLatch countDownLatch;
    private ExecutorService executorService;
    private FutureTask<String> future;

    @Autowired
    private Ignite ignite;

    public IgniteCacheLockTest() {

        this.cacheName = "TestCacheName";
        this.qualifier = "TestCacheQualifier";
        this.key = "key";
    }

    @BeforeEach
    public void setUp() throws Exception {

        this.countDownLatch = new CountDownLatch(1);
        this.executorService = Executors.newFixedThreadPool(1);

        this.future = new FutureTask<>(() -> {

            Lock lock = igniteProvider.lock(key);
            if (!lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                countDownLatch.countDown();
            }
            return null;
        });

        this.igniteProvider = new IgniteProvider<>(ignite, qualifier, () -> {

            CacheConfiguration<String, String> cacheConfiguration = new CacheConfiguration<>();
            cacheConfiguration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
            cacheConfiguration.setName(cacheName);
            cacheConfiguration.setBackups(1);
            return cacheConfiguration;
        });
    }

    @AfterEach
    public void tearDown() throws Exception {

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
        }
    }

    @Test
    public void testLock() throws Exception {

        Lock lock = igniteProvider.cache().lock(key);
        lock.lock();
        try {
            executorService.execute(future);
            if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
                LOGGER.error("Testing lock failed");
                throw new TimeoutException();
            }
            LOGGER.error("Testing lock success");
        } finally {
            lock.unlock();
        }
    }
}
