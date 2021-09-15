package com.github.mimsic.base.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonTypeReferenceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonTypeReferenceTest.class);

    private TypeReference<Data<String>> typeReference;
    private String typeReferenceName;

    @BeforeEach
    public void setUp() throws Exception {
        typeReference = new TypeReference<Data<String>>() {
        };
        typeReferenceName = typeReference.getType().getTypeName();
    }

    @Test
    public void testTypeReferenceAccess() {
        Map<String, String> testMap = new ConcurrentHashMap<>();
        for (int i = 0; i < 1000; i++) {
            testMap.put(typeReferenceName + i, "value=" + i);
        }
        long before = System.nanoTime();
        String result = testMap.get(typeReferenceName + 500);
        long after = System.nanoTime();
        Assertions.assertEquals("value=500", result);
        LOGGER.info("Map access time {} nano seconds with long String key returns: {}", after - before, result);
    }

    @Test
    public void testTypeReference() {
        Assertions.assertEquals(typeReferenceName, typeReference.getType().getTypeName());
    }

    private static class Data<T> {

        public Data() {
        }

        public Data(T field) {
            this.field = field;
        }

        private T field;

        public T getField() {
            return field;
        }

        public void setField(T field) {
            this.field = field;
        }
    }
}
