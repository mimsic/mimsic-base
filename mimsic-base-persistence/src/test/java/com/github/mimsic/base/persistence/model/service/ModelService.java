package com.github.mimsic.base.persistence.model.service;


import com.github.mimsic.base.common.provider.Provider;
import com.github.mimsic.base.persistence.model.entity.Model;

public interface ModelService {

    Model save(Provider<Long, Model> provider, Model model);

    void delete(Provider<Long, Model> provider, Model model);

    Model save(String schema, Model model);

    void delete(String schema, Model model);
}
