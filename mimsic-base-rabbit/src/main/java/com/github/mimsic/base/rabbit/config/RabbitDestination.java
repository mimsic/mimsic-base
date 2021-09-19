package com.github.mimsic.base.rabbit.config;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class RabbitDestination {

    private final String exchangeName;
    private final String queueName;
    private final String qualifier;
    private final String routingKey;
}
