package com.github.mimsic.base.persistence.model.repository;

import com.github.mimsic.base.persistence.model.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
}
