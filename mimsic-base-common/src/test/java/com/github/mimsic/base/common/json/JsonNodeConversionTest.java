package com.github.mimsic.base.common.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.mimsic.base.common.message.MessageField;
import com.github.mimsic.base.common.message.MessageWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class JsonNodeConversionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonNodeConversionTest.class);

    @Test
    public void testJsonNodeConversion() throws IOException {

        String json = ObjectMapperUtil.writeValueAsSting(new MessageWrapper<>("test-msgType"));

        LOGGER.info("Serialized Message: {}", json);

        JsonNode jsonNode = ObjectMapperUtil.readValue(json, JsonNode.class);

        String msgType = Optional.ofNullable(jsonNode.get(MessageField.msgType.key()))
                .orElseGet(NullNode::getInstance).asText();

        Assertions.assertEquals("test-msgType", msgType);
    }
}
