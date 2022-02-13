package com.github.mimsic.base.persistence.aop;

import com.github.mimsic.base.common.provider.Provider;
import com.github.mimsic.base.persistence.model.entity.Model;
import com.github.mimsic.base.persistence.model.provider.impl.ModelProvider;
import com.github.mimsic.base.persistence.model.service.ModelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SchemaRoutingAspectTest {

    private String schema = "test-schema";

    @Autowired
    private ModelService service;

    @Test
    public void testCachedSchemaRoutingAspect() {

        Model model = new Model();
        Provider<Long, Model> provider = new ModelProvider<>(schema);
        model = service.save(provider, model);
        Assertions.assertEquals(schema, model.getSchema());
        service.delete(provider, model);
    }

    @Test
    public void testSimpleSchemaRoutingAspect() {

        Model model = new Model();
        model = service.save(schema, model);
        Assertions.assertEquals(schema, model.getSchema());
        service.delete(schema, model);
    }
}