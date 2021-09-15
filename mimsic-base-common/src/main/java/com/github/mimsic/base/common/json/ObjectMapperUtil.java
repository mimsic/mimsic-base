package com.github.mimsic.base.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class ObjectMapperUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return objectMapper.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> valueTypeRef) {
        return objectMapper.convertValue(fromValue, valueTypeRef);
    }

    public static ArrayNode createArrayNode(int capacity) {
        return objectMapper.getDeserializationConfig().getNodeFactory().arrayNode(capacity);
    }

    public static ArrayNode createArrayNode() {
        return objectMapper.getDeserializationConfig().getNodeFactory().arrayNode();
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static JsonNode readTree(String content) throws IOException {
        return objectMapper.readTree(content);
    }

    public static <T> T readValue(String content, Class<T> valueType) throws IOException {
        return objectMapper.readValue(content, valueType);
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) throws IOException {
        return objectMapper.readValue(content, valueTypeRef);
    }

    public static <T> T treeToValue(TreeNode fromNode, Class<T> valueType) throws IOException {
        return objectMapper.treeToValue(fromNode, valueType);
    }

    public static <T extends JsonNode> T valueToTree(Object fromValue) {
        return objectMapper.valueToTree(fromValue);
    }

    public static String writeValueAsSting(Object value) throws IOException {
        return objectMapper.writeValueAsString(value);
    }
}
