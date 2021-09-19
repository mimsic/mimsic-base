package com.github.mimsic.base.web.websocket.config;

import com.github.mimsic.base.web.websocket.endpoint.SecurityHandshakeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
public class WebSocketBeanConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketBeanConfigurer.class);

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Bean(name = "HttpSessionHandshakeInterceptor")
    public HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HttpSessionHandshakeInterceptor();
    }

    @Bean(name = "SecurityHandshakeInterceptor")
    public SecurityHandshakeInterceptor securityHandshakeInterceptor() {
        return new SecurityHandshakeInterceptor();
    }

    @Bean
    @ConditionalOnProperty(prefix = "websocket-config", value = "container-configurer-enabled")
    public ServletServerContainerFactoryBean servletServerContainerFactoryBean() {

        ServletServerContainerFactoryBean serverContainerFactory = new ServletServerContainerFactoryBean();
        serverContainerFactory.setMaxBinaryMessageBufferSize(webSocketConfig.getContainer().getMaxBinaryMessageBufferSize());
        serverContainerFactory.setMaxTextMessageBufferSize(webSocketConfig.getContainer().getMaxTextMessageBufferSize());
        LOGGER.info("Servlet Server Container Factory instantiated.");
        return serverContainerFactory;
    }
}
