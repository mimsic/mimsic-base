package com.github.mimsic.base.ignite.provider;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class IgniteVmArgumentTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgniteVmArgumentTest.class);

    @Test
    void testVmArguments() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        LOGGER.info("VM arguments are {}", arguments);
    }
}
