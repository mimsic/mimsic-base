package com.github.mimsic.base.web.server.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Optional;
import java.util.Set;

@Configuration
public class ServerConnectorConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectorConfigurer.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ServerConfig serverConfig;

    @Bean
    @ConditionalOnProperty(prefix = "server-config", value = "connector-configurer-enabled")
    public ServletWebServerFactory servletWebServerFactory() {

        TomcatServletWebServerFactory webServerFactory = new CustomTomcatServletWebServerFactory(
                serverConfig.getConstraints().getPatterns(),
                serverConfig.getConstraints().getUserConstraint());

        serverConfig.getConnectors().forEach(connectorUnit -> {
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setPort(connectorUnit.getPort());
            connector.setScheme(connectorUnit.getScheme());
            connector.setSecure(connectorUnit.isSecure());
            connector.setRedirectPort(connectorUnit.getRedirectPort());

            Optional.ofNullable(connectorUnit.getKeystore()).ifPresent(keystore -> {
                Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
                try {
                    Resource resource = resourceLoader.getResource(keystore.getPath());
                    protocol.setKeystoreFile(resource.getFile().getAbsolutePath());
                    protocol.setKeystorePass(keystore.getPassword());
                    // protocol.setTruststoreFile(keystoreFile(keystore.getPath()));
                    // protocol.setTruststorePass(keystore.getPassword());
                    protocol.setKeyAlias(keystore.getAlias());
                    protocol.setKeystoreType(keystore.getType());
                    protocol.setSSLEnabled(connectorUnit.isSslEnabled());
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to access keystore or truststore: [" + keystore.getPath() + "]", ex);
                }
            });
            webServerFactory.addAdditionalTomcatConnectors(connector);
        });
        LOGGER.info("Servlet Web Server Factory instantiated.");
        return webServerFactory;
    }

    private static class CustomTomcatServletWebServerFactory extends TomcatServletWebServerFactory {

        private Set<String> patterns;
        private String userConstraint;

        public CustomTomcatServletWebServerFactory(Set<String> patterns, String userConstraint) {
            this.patterns = patterns;
            this.userConstraint = userConstraint;
        }

        @Override
        protected void postProcessContext(Context context) {
            SecurityCollection securityCollection = new SecurityCollection();
            patterns.forEach(securityCollection::addPattern);
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setUserConstraint(userConstraint);
            securityConstraint.addCollection(securityCollection);
            context.addConstraint(securityConstraint);
        }
    }
}


