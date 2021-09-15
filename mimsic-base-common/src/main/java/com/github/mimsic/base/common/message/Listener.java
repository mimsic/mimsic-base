package com.github.mimsic.base.common.message;

public interface Listener<T> {

    boolean accept(T message);
}
