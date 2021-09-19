package com.github.mimsic.base.rabbit.receiver;

import com.github.mimsic.base.common.message.Listener;
import com.github.mimsic.base.common.message.Receiver;
import com.github.mimsic.base.rabbit.config.RabbitDestination;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;

public class RabbitReceiver<T> implements Receiver<T> {

    protected final SimpleRabbitListenerContainerFactory containerFactory;
    protected final MessageConverter messageConverter;

    protected final String qualifier;
    protected final String queueName;

    public RabbitReceiver(
            SimpleRabbitListenerContainerFactory containerFactory,
            MessageConverter messageConverter,
            RabbitDestination rabbitDestination) {

        this.containerFactory = containerFactory;
        this.messageConverter = messageConverter;

        this.qualifier = rabbitDestination.getQualifier();
        this.queueName = rabbitDestination.getQueueName();
    }

    @Override
    public String qualifier() {
        return qualifier;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AutoCloseable listener(Listener<T> listener) {
        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
        endpoint.setMessageListener(message -> {
            listener.accept((T) messageConverter.fromMessage(message));
        });
        endpoint.setQueueNames(queueName);
        SimpleMessageListenerContainer container = containerFactory.createListenerContainer(endpoint);
        container.start();
        return container::stop;
    }
}
