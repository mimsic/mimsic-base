package com.github.mimsic.base.rabbit.sender;

import com.github.mimsic.base.common.message.Sender;
import com.github.mimsic.base.rabbit.config.RabbitDestination;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitSender<T> implements Sender<T> {

    protected final RabbitTemplate rabbitTemplate;

    protected final String exchangeName;
    protected final String qualifier;
    protected final String routingKey;

    public RabbitSender(
            RabbitTemplate rabbitTemplate,
            RabbitDestination rabbitDestination) {

        this.rabbitTemplate = rabbitTemplate;

        this.exchangeName = rabbitDestination.getExchangeName();
        this.qualifier = rabbitDestination.getQualifier();
        this.routingKey = rabbitDestination.getRoutingKey();
    }

    @Override
    public String qualifier() {
        return qualifier;
    }

    @Override
    public void send(T message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
