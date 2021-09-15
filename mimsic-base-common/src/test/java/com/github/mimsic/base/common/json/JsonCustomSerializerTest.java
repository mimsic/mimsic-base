package com.github.mimsic.base.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class JsonCustomSerializerTest {

    private static final String PropertyName = "customProperty";
    private static final String CustomValue = "customValue";
    private static final String BaseValue = "baseValue";

    @Test
    public void testCustomSerializerModifier_omitField() throws JsonProcessingException {

        SerializerFactory serializerFactory = BeanSerializerFactory
                .instance.withSerializerModifier(new CustomBeanSerializerModifier_omitField());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializerFactory(serializerFactory);
        Assertions.assertEquals(
                "{\"field\":{\"someArray\":[\"one\",\"two\"]}}",
                objectMapper.writeValueAsString(new Data<>(new Model())));
    }

    @Test
    public void testCustomSerializerModifier_replaceField() throws JsonProcessingException {

        SerializerFactory serializerFactory = BeanSerializerFactory
                .instance.withSerializerModifier(new CustomBeanSerializerModifier_replaceField());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializerFactory(serializerFactory);
        Assertions.assertEquals(
                "{\"field\":{\"customProperty\":\"customValue\",\"someArray\":[\"one\",\"two\"]}}",
                objectMapper.writeValueAsString(new Data<>(new Model())));
    }

    private static class CustomSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(
                Object value,
                JsonGenerator jsonGenerator,
                SerializerProvider provider) throws IOException, JsonProcessingException {

            String customValue = CustomValue; // someService.getCustomValue(value);
            jsonGenerator.writeString(customValue);
        }
    }

    private static class CustomBeanSerializerModifier_omitField extends BeanSerializerModifier {

        public List<BeanPropertyWriter> changeProperties(
                SerializationConfig config,
                BeanDescription beanDesc,
                List<BeanPropertyWriter> beanProperties) {

            beanProperties.removeIf(beanPropertyWriter -> PropertyName.equals(beanPropertyWriter.getName()));
            return beanProperties;
        }
    }

    private static class CustomBeanSerializerModifier_replaceField extends BeanSerializerModifier {

        public List<BeanPropertyWriter> changeProperties(
                SerializationConfig config,
                BeanDescription beanDesc,
                List<BeanPropertyWriter> beanProperties) {

            for (BeanPropertyWriter beanPropertyWriter : beanProperties) {
                if (PropertyName.equals(beanPropertyWriter.getName())) {
                    beanPropertyWriter.assignSerializer(new CustomSerializer());
                }
            }
            return beanProperties;
        }
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

    private static class Model {

        private String customProperty = BaseValue;
        private String[] someArray = new String[]{"one", "two"};

        public String getCustomProperty() {
            return customProperty;
        }

        public void setCustomProperty(String customProperty) {
            this.customProperty = customProperty;
        }

        public String[] getSomeArray() {
            return someArray;
        }

        public void setSomeArray(String[] someArray) {
            this.someArray = someArray;
        }
    }
}
