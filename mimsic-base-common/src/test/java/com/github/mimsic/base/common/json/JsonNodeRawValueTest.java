package com.github.mimsic.base.common.json;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mimsic.base.common.message.MessageWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class JsonNodeRawValueTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonNodeRawValueTest.class);

    @Test
    public void testJsonNodeAddingJsonString() throws IOException {

        ResourceBundle bundle = ResourceBundle.getBundle("resource");

        ObjectNode node = ObjectMapperUtil.createObjectNode();
        Enumeration<String> keys = bundle.getKeys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            node.put(key, bundle.getString(key));
        }

        String value = ObjectMapperUtil.writeValueAsSting(node);

        Data<String> data = new Data<>(value);
        MessageWrapper<Data<String>, MetaData<String>> wrapper = new MessageWrapper<>(data, "msgType");

        RawData<String> rawData = new RawData<>(value);
        MessageWrapper<RawData<String>, MetaData<String>> rawWrapper = new MessageWrapper<>(rawData, "msgType");

        String serializedWrapper = ObjectMapperUtil.writeValueAsSting(wrapper);
        LOGGER.info("serialized wrapper: {}", serializedWrapper);
        LOGGER.info("deserialized wrapper: {}", ObjectMapperUtil.readTree(serializedWrapper));

        String serializedRawWrapper = ObjectMapperUtil.writeValueAsSting(rawWrapper);
        LOGGER.info("serialized raw wrapper: {}", serializedRawWrapper);
        LOGGER.info("deserialized raw wrapper: {}", ObjectMapperUtil.readTree(serializedRawWrapper));
    }

    @NoArgsConstructor
    @Getter
    @Setter
    private static class Data<T> {

        private T value;

        public Data(T value) {
            this.value = value;
        }

    }

    @NoArgsConstructor
    @Getter
    @Setter
    private static class RawData<T> {

        @JsonRawValue
        private T value;

        public RawData(T value) {
            this.value = value;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    private static class MetaData<T> {

        private T value;

        public MetaData(T value) {
            this.value = value;
        }

    }
}
