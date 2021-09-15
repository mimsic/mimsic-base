package com.github.mimsic.base.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.StreamSupport;

public class ArrayNodeToSetTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayNodeToSetTest.class);

    @Test
    public void testArrayNodeToSet() throws IOException {

        ArrayNode arrayNode = ObjectMapperUtil.createArrayNode();

        String item = "test 2500";
        int total = 5000;

        for (int i = 0; i < total; i++) {
            arrayNode.add(String.format("test %s", i));
        }

        for (int j = 0; j < 10; j++) {

            long timeStamp1 = System.nanoTime();

            boolean streamResult = StreamSupport.stream(arrayNode.spliterator(), false)
                    .anyMatch(value -> value.asText().equals(item));

            long timeStamp2 = System.nanoTime();

            Set<String> set = ObjectMapperUtil.convertValue(arrayNode, new TypeReference<HashSet<String>>() {
            });
            boolean setResult = set.contains(item);

            long timeStamp3 = System.nanoTime();

            LOGGER.info("Time taken by Stream anyMatch in nanos: {}", timeStamp2 - timeStamp1);
            LOGGER.info("Time taken by Set contains in nanos: {}", timeStamp3 - timeStamp2);

            Assertions.assertTrue(streamResult);
            Assertions.assertTrue(setResult);
        }
    }
}
