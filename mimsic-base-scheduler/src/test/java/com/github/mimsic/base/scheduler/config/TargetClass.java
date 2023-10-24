package com.github.mimsic.base.scheduler.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class TargetClass {

    private static final Logger LOGGER = LoggerFactory.getLogger(TargetClass.class);

    private CountDownLatch latch;

    public TargetClass() {
    }

    public TargetClass(CountDownLatch latch) {
        this.latch = latch;
    }

    public void scheduledOperation() {
        if (latch != null) {
            latch.countDown();
        }
        LOGGER.info("Scheduled operation called.");
    }
}
