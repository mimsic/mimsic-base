package com.github.mimsic.base.common.message;

public interface Sender<T> {

    String qualifier();

    void send(T message);
}
