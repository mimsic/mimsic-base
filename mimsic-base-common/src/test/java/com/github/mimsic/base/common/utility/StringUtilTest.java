package com.github.mimsic.base.common.utility;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class StringUtilTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtilTest.class);

    @Test
    public void testStringSplitSpeed() {

        String data = "ABC.CDE.FGH";

        long timeStamp1 = System.nanoTime();
        String[] result1 = data.split(RegexDelimiter.DOT);
        long timeStamp2 = System.nanoTime();
        String[] result2 = StringUtils.split(data, StringDelimiter.DOT);
        long timeStamp3 = System.nanoTime();
        String[] result3 = StringUtil.splitByCharArray(data, CharDelimiter.DOT);
        long timeStamp4 = System.nanoTime();
        String[] result4 = StringUtil.splitByIndex(data, StringDelimiter.DOT);
        long timeStamp5 = System.nanoTime();

        LOGGER.info("Time taken by Java split function in nanos: {}", timeStamp2 - timeStamp1);
        LOGGER.info("Result by Java split function: {}", Arrays.toString(result1));
        LOGGER.info("Time taken by Apache split function in nanos: {}", timeStamp3 - timeStamp2);
        LOGGER.info("Result by Apache split function: {}", Arrays.toString(result2));
        LOGGER.info("Time taken by Custom split function in nanos: {}", timeStamp4 - timeStamp3);
        LOGGER.info("Result by Custom split function: {}", Arrays.toString(result3));
        LOGGER.info("Time taken by IndexOf split function in nanos: {}", timeStamp5 - timeStamp4);
        LOGGER.info("Result by IndexOf split function: {}", Arrays.toString(result4));

        Assertions.assertArrayEquals(new String[]{"ABC", "CDE", "FGH"}, result3);
        Assertions.assertArrayEquals(new String[]{"ABC", "CDE", "FGH"}, result4);
    }
}