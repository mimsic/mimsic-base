package com.github.mimsic.base.rabbit.config;

import com.github.mimsic.base.common.message.ErrorMessage;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Configuration
@Profile("rabbit")
public class RabbitDestinationConfigurer implements InitializingBean {

    private final Map<String, BiFunction<String, RabbitConfig.ExchangeInfo, Exchange>> functions = new HashMap<>();
    private final Map<String, RabbitDestination> destinations = new HashMap<>();

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitConfig rabbitConfig;

    @Override
    public void afterPropertiesSet() throws Exception {

        functions.put("direct", this::directExchange);
        functions.put("fanout", this::fanoutExchange);
        functions.put("headers", this::headersExchange);
        functions.put("topic", this::topicExchange);

        rabbitConfig.getExchanges().forEach((exchangeName, exchangeInfo) -> {

            Exchange exchange = Optional.ofNullable(functions.get(Optional.ofNullable(exchangeInfo.getType())
                    .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessage.NULL_VALUE, exchangeName)))))
                    .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessage.INVALID_VALUE, exchangeName)))
                    .apply(exchangeName, exchangeInfo);
            declareAndBindQueues(exchange, exchangeInfo.getQueues());
        });
    }

    private void declareAndBindQueues(Exchange exchange, Map<String, RabbitConfig.QueueInfo> queues) {

        amqpAdmin.declareExchange(exchange);
        queues.forEach((queueName, queueInfo) -> {

            Queue queue = new Queue(queueName, queueInfo.isDurable(), queueInfo.isExclusive(), queueInfo.isAutoDelete());
            amqpAdmin.declareQueue(queue);
            amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(queueInfo.getRoutingKey()).noargs());
            destinations.put(queueInfo.getQualifier(),
                    new RabbitDestination(
                            exchange.getName(),
                            queueName,
                            queueInfo.getQualifier(),
                            queueInfo.getRoutingKey()));
        });
    }

    public RabbitDestination destination(String qualifier) {
        return Optional.ofNullable(destinations.get(qualifier))
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessage.INVALID_VALUE, qualifier)));
    }

    private Exchange directExchange(String exchangeName, RabbitConfig.ExchangeInfo exchangeInfo) {
        return new DirectExchange(exchangeName, exchangeInfo.isDurable(), exchangeInfo.isAutoDelete());
    }

    private Exchange fanoutExchange(String exchangeName, RabbitConfig.ExchangeInfo exchangeInfo) {
        return new FanoutExchange(exchangeName, exchangeInfo.isDurable(), exchangeInfo.isAutoDelete());
    }

    private Exchange headersExchange(String exchangeName, RabbitConfig.ExchangeInfo exchangeInfo) {
        return new HeadersExchange(exchangeName, exchangeInfo.isDurable(), exchangeInfo.isAutoDelete());
    }

    private Exchange topicExchange(String exchangeName, RabbitConfig.ExchangeInfo exchangeInfo) {
        return new TopicExchange(exchangeName, exchangeInfo.isDurable(), exchangeInfo.isAutoDelete());
    }
}
