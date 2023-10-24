package com.github.mimsic.base.web.server.config;

import com.github.mimsic.base.web.common.config.Keystore;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
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

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Configuration
public class ServerConnectorConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectorConfigurer.class);

    private final ResourceLoader resourceLoader;

    private final ServerConfig serverConfig;

    @Autowired
    public ServerConnectorConfigurer(ResourceLoader resourceLoader, ServerConfig serverConfig) {
        this.resourceLoader = resourceLoader;
        this.serverConfig = serverConfig;
    }

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
                    SSLHostConfig sslHostConfig = SslHostConfig(keystore, resource);
                    protocol.addSslHostConfig(sslHostConfig);
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

    private static SSLHostConfig SslHostConfig(Keystore keystore, Resource resource) throws IOException {

        SSLHostConfig sslHostConfig = new SSLHostConfig();
        SSLHostConfigCertificate sslHostConfigCertificate = new SSLHostConfigCertificate(
                sslHostConfig,
                SSLHostConfigCertificate.Type.UNDEFINED);
        sslHostConfigCertificate.setCertificateKeyAlias(keystore.getAlias());
        sslHostConfigCertificate.setCertificateKeystoreFile(resource.getFile().getAbsolutePath());
        sslHostConfigCertificate.setCertificateKeystorePassword(keystore.getPassword());
        sslHostConfigCertificate.setCertificateKeystoreType(keystore.getType());
        sslHostConfig.addCertificate(sslHostConfigCertificate);
        return sslHostConfig;
    }

    private static class CustomTomcatServletWebServerFactory extends TomcatServletWebServerFactory {

        private final Set<String> patterns;
        private final String userConstraint;

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


