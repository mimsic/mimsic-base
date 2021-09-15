package com.github.mimsic.base.common.message;

public interface Receiver<T> {

    String qualifier();

    AutoCloseable listener(Listener<T> listener);
}
