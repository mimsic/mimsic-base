package com.github.mimsic.base.web.server.config;

import com.github.mimsic.base.web.common.config.Keystore;
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
@ConfigurationProperties(prefix = "server-config")
@Getter
@Setter
@NoArgsConstructor
public class ServerConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConfig.class);

    private Set<ConnectorUnit> connectors;
    private Constraints constraints;

    private boolean connectorConfigurerEnabled;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info(toString());
    }

    @Override
    public String toString() {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append("Server Config: {").append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("connectorConfigurerEnabled: ").append(connectorConfigurerEnabled).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("connectors: ").append(connectors).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("constraints: ").append(constraints).append('\n');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ConnectorUnit {

        private Keystore keystore;
        private String scheme;
        private int port;
        private int redirectPort;
        private boolean secure;
        private boolean sslEnabled;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("scheme: ").append(scheme).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("port: ").append(port).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("redirectPort: ").append(redirectPort).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("secure: ").append(secure).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("sslEnabled: ").append(sslEnabled).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("keystore: ").append(keystore).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 3));
            return stringBuilder.toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Constraints {

        private String userConstraint;
        private Set<String> patterns;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("userConstraint: ").append(userConstraint).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("patterns: ").append(patterns);
            return stringBuilder.toString();
        }
    }
}
