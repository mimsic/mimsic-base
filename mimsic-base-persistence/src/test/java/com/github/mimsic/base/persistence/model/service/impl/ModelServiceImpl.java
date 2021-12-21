package com.github.mimsic.base.persistence.model.service.impl;

import com.github.mimsic.base.common.provider.Provider;
import com.github.mimsic.base.persistence.aop.CachedSchemaRouting;
import com.github.mimsic.base.persistence.aop.SimpleSchemaRouting;
import com.github.mimsic.base.persistence.config.SchemaRoutingContextHolder;
import com.github.mimsic.base.persistence.model.entity.Model;
import com.github.mimsic.base.persistence.model.repository.ModelRepository;
import com.github.mimsic.base.persistence.model.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelRepository repository;

    @CachedSchemaRouting
    public Model save(Provider<Long, Model> provider, Model model) {
        model.setSchema(SchemaRoutingContextHolder.getSchema());
        model = repository.save(model);
        provider.save(model.getId(), model);
        return model;
    }

    @CachedSchemaRouting
    public void delete(Provider<Long, Model> provider, Model model) {
        repository.delete(model);
        provider.delete(model.getId());
    }

    @SimpleSchemaRouting
    public Model save(String schema, Model model) {
        model.setSchema(SchemaRoutingContextHolder.getSchema());
        return repository.save(model);
    }

    @SimpleSchemaRouting
    public void delete(String schema, Model model) {
        repository.delete(model);
    }
}
