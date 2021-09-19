package com.github.mimsic.base.rabbit.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "rabbit-config")
@Profile("rabbit")
@Getter
@Setter
@NoArgsConstructor
public class RabbitConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfig.class);

    private Map<String, ExchangeInfo> exchanges;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info(toString());
    }

    @Override
    public String toString() {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append("Rabbit Config: {").append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("exchanges: ").append(exchanges).append('\n');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ExchangeInfo {

        private Map<String, QueueInfo> queues;

        private String type;

        private boolean autoDelete;
        private boolean durable;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("autoDelete: ").append(autoDelete).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("durable: ").append(durable).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("queues: ").append(queues).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("type: ").append(type).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 3));
            return stringBuilder.toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class QueueInfo {

        private String qualifier;
        private String routingKey;

        private boolean autoDelete;
        private boolean durable;
        private boolean exclusive;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9)).append("autoDelete: ").append(autoDelete).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9)).append("exclusive: ").append(exclusive).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9)).append("durable: ").append(durable).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9)).append("qualifier: ").append(qualifier).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9)).append("routingKey: ").append(routingKey).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6));
            return stringBuilder.toString();
        }
    }
}
