package com.github.mimsic.base.ignite.sender;

import com.github.mimsic.base.common.message.Sender;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteMessaging;

import java.util.Optional;
import java.util.function.Supplier;

public class IgniteSender<T> implements Sender<T> {

    protected final IgniteMessaging messaging;
    protected final String qualifier;

    protected final Object topic;

    public IgniteSender(Ignite ignite, String qualifier, Supplier<Object> configuration) {

        Optional.ofNullable(ignite).orElseThrow(() -> new IllegalArgumentException("Ignite is null"));
        Optional.ofNullable(qualifier).orElseThrow(() -> new IllegalArgumentException("Qualifier is null"));

        this.messaging = ignite.message();
        this.qualifier = qualifier;
        this.topic = configuration.get();
    }

    @Override
    public String qualifier() {
        return qualifier;
    }

    @Override
    public void send(T message) {
        messaging.send(topic, message);
    }
}
