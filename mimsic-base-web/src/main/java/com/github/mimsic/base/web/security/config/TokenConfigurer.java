package com.github.mimsic.base.web.security.config;

import com.github.mimsic.base.web.security.authentication.UserExtended;
import com.github.mimsic.base.web.common.config.Keystore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class TokenConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenConfigurer.class);

    private final ResourceLoader resourceLoader;

    private final SecurityConfig securityConfig;

    @Autowired
    public TokenConfigurer(ResourceLoader resourceLoader, SecurityConfig securityConfig) {
        this.resourceLoader = resourceLoader;
        this.securityConfig = securityConfig;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {

        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setAccessTokenConverter(new DefaultAccessTokenConverter() {

            @Override
            public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
                OAuth2Authentication authentication = super.extractAuthentication(map);
                authentication.setDetails(map);
                return authentication;
            }
        });

        Keystore keystore = securityConfig.getToken().getSigningKeystore();
        if (keystore != null) {
            try {
                Resource resource = resourceLoader.getResource(keystore.getPath());
                KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(resource, keystore.getPassword().toCharArray());
                tokenConverter.setKeyPair(keyFactory.getKeyPair(keystore.getAlias()));
                return tokenConverter;
            } catch (Exception ex) {
                LOGGER.warn("Failed to access keystore: [" + keystore.getPath() + "]", ex);
            }
        }
        tokenConverter.setSigningKey(securityConfig.getToken().getSigningKey());
        LOGGER.info("Using symmetric signing key");
        return tokenConverter;
    }

    @Bean
    @Autowired
    public TokenEnhancer tokenEnhancer(JwtAccessTokenConverter jwtAccessTokenConverter) {

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new TokenEnhancer() {

            @Override
            public OAuth2AccessToken enhance(
                    OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

                Object principal = authentication.getPrincipal();
                if (principal instanceof UserExtended) {
                    ((DefaultOAuth2AccessToken) accessToken)
                            .setAdditionalInformation(((UserExtended) principal).getClaims());
                } else {
                    Authentication userAuthentication = authentication.getUserAuthentication();
                    if (userAuthentication != null) {
                        principal = userAuthentication.getPrincipal();
                        if (principal instanceof UserExtended) {
                            ((DefaultOAuth2AccessToken) accessToken)
                                    .setAdditionalInformation(((UserExtended) principal).getClaims());
                        }
                    }
                }
                return accessToken;
            }
        }, jwtAccessTokenConverter));
        return tokenEnhancerChain;
    }

    @Bean
    @Autowired
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }
}
