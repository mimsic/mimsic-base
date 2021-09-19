package com.github.mimsic.base.rabbit.config;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("rabbit")
public class RabbitBeanConfigurer {

    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }
}
