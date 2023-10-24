package com.github.mimsic.base.scheduler.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SchedulerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerTest.class);

    @Autowired
    private SchedulerConfig schedulerConfig;

    @Autowired
    private SchedulerConfigurer schedulerConfigurer;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void testCronScheduler() {

        CountDownLatch latch = new CountDownLatch(schedulerConfig.getSchedulers().size());
        schedulerConfig.getSchedulers().forEach((SchedulerConfig.SchedulerUnit schedulerUnit) -> {

            TargetClass targetObject = new TargetClass(latch);
            schedulerConfigurer.registerCronTask(targetObject::scheduledOperation, schedulerUnit);
        });
        try {
            if (!latch.await(10000, TimeUnit.MILLISECONDS)) {
                Assertions.fail("Failed to execute cron job");
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to schedule cron job.", ex);
        }
    }
}
