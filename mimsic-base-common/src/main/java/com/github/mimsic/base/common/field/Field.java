package com.github.mimsic.base.common.field;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Field {

    @EqualsAndHashCode.Include
    protected final String key;

    public Field(String key) {
        this.key = key;
    }
}
