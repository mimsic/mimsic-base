package com.github.mimsic.base.common.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringPaddingTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringPaddingTest.class);

    /*
     * Using stringFormat
     * "The is %1s %2$-10s test."
     */
    @Test
    public void testRightPadding() {
        int padding = 10;
        String result = String.format("The is %1s %2$-" + padding + "s test.", "for", "padding");
        LOGGER.info(result);
        Assertions.assertEquals("The is for padding    test.", result);
    }

    /*
     * "The is %1s %2$10s test."
     */
    @Test
    public void testLeftPadding() {
        int padding = 10;
        String result = String.format("The is %1s %2$" + padding + "s test.", "for", "padding");
        LOGGER.info(result);
        Assertions.assertEquals("The is for    padding test.", result);
    }
}
