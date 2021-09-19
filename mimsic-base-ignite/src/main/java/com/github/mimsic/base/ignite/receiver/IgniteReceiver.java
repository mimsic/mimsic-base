package com.github.mimsic.base.ignite.receiver;

import com.github.mimsic.base.common.message.Listener;
import com.github.mimsic.base.common.message.Receiver;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.IgniteState;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteBiPredicate;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class IgniteReceiver<T> implements Receiver<T> {

    protected final IgniteMessaging messaging;
    protected final String qualifier;

    protected final Object topic;

    public IgniteReceiver(Ignite ignite, String qualifier, Supplier<Object> configuration) {

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
    public AutoCloseable listener(Listener<T> listener) {
        IgniteBiPredicate<UUID, ?> biPredicate = (UUID uuid, T message) -> listener.accept(message);
        messaging.localListen(topic, biPredicate);
        return () -> {
            IgniteState state = Ignition.state();
            if (IgniteState.STARTED == state) {
                messaging.stopLocalListen(topic, biPredicate);
            }
        };
    }
}
