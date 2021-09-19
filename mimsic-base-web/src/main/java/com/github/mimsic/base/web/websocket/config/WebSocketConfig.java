package com.github.mimsic.base.web.websocket.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "websocket-config")
@Getter
@Setter
@NoArgsConstructor
public class WebSocketConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

    private Set<String> allowedOrigins;
    private Container container;
    private int sendBufferSizeLimit;
    private int sendTimeLimit;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info(toString());
    }

    @Override
    public String toString() {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append("WebSocket Configuration: {").append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("allowedOrigins: ").append(allowedOrigins).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("container: ").append(container).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("sendBufferSizeLimit: ").append(sendBufferSizeLimit).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("sendTimeLimit: ").append(sendTimeLimit).append('\n');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class Container {
        private Integer maxBinaryMessageBufferSize;
        private Integer maxTextMessageBufferSize;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("maxBinaryMessageBufferSize: ");
            stringBuilder.append(maxBinaryMessageBufferSize).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("maxTextMessageBufferSize: ");
            stringBuilder.append(maxTextMessageBufferSize);
            return stringBuilder.toString();
        }
    }
}
