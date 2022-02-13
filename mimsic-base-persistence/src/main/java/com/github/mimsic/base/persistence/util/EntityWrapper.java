package com.github.mimsic.base.persistence.util;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class EntityWrapper<T, U> {

    private T entity;
    private U metadata;

    public EntityWrapper(T entity) {
        this.entity = entity;
    }
}
