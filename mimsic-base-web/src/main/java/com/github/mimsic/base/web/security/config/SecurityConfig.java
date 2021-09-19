package com.github.mimsic.base.web.security.config;

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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "security-config")
@Getter
@Setter
@NoArgsConstructor
public class SecurityConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    private Set<CorsUnit> cors;

    private AuthorizationServer authorizationServer;
    private ResourceServer resourceServer;
    private Token token;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info(toString());
    }

    @Override
    public String toString() {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append("Security Config: {").append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("authorizationServer: ").append(authorizationServer).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("cors: ").append(cors).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("resourceServer: ").append(resourceServer).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("token: ").append(token).append('\n');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CorsUnit {

        private List<String> allowedOrigins;
        private List<String> allowedHeaders;
        private List<String> allowedMethods;
        private String allowedPath;
        private boolean allowCredentials;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("allowedOrigins: ").append(allowedOrigins).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("allowedHeaders: ").append(allowedHeaders).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("allowedMethods: ").append(allowedMethods).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("allowedPath: ").append(allowedPath).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("allowCredentials: ").append(allowCredentials).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 3));
            return stringBuilder.toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AuthorizationServer {

        private String[] grantTypes;
        private String[] scopes;
        private String client;
        private String secret;
        private int accessTokenValidity;
        private int refreshTokenValidity;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("grantTypes: ").append(Arrays.toString(grantTypes)).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("scopes: ").append(Arrays.toString(scopes)).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("client: ").append(client).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("secret: ").append(secret).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("accessTokenValidity: ").append(accessTokenValidity).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("refreshTokenValidity: ").append(refreshTokenValidity);
            return stringBuilder.toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ResourceServer {

        private String resourceId;
        private boolean stateless;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("resourceId: ").append(resourceId).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("stateless: ").append(stateless);
            return stringBuilder.toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Token {

        private Keystore signingKeystore;
        private String signingKey;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("signingKeystore: ").append(signingKeystore).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("signingKey: ").append(signingKey);
            return stringBuilder.toString();
        }
    }
}
