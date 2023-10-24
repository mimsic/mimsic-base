package com.github.mimsic.base.web.security.authentication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.mimsic.base.common.utility.ObjectUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Map;
import java.util.Set;

public class AuthenticationUtil {

    private static final TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
    };

    public static Map<String, Object> details(Authentication authentication) {

        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            Object details = oAuth2Authentication.getDetails();
            if (details instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;
                return ((Map<String, Object>) oAuth2AuthenticationDetails.getDecodedDetails());
            }
        }
        try {
            return ObjectUtil.convertToMap(authentication.getDetails(), typeReference);
        } catch (Exception ex) {
            throw new AuthenticationServiceException("Failed to convert authentication details", ex);
        }
    }

    public static String schema(Authentication authentication) {

        Object schema = details(authentication).get(AuthenticationField.schema.key());
        if (schema == null) {
            throw new AuthenticationServiceException("No schema parameter found in authentication details");
        }
        return (String) schema;
    }

    public static String schema(Authentication authentication, Set<String> schemas) throws AuthenticationServiceException {

        String schema = schema(authentication);
        if (schemas.contains(schema)) {
            return schema;
        }
        throw new AuthenticationServiceException("Invalid schema parameter found in authentication details");
    }

    public static String schema(HttpServletRequest request) throws AuthenticationServiceException {

        String schema = request.getParameter(AuthenticationField.schema.key());
        if (schema == null) {
            throw new AuthenticationServiceException("No schema parameter found in request context");
        }
        return schema;
    }

    public static String schema(HttpServletRequest request, Set<String> schemas) throws AuthenticationServiceException {

        String schema = schema(request);
        if (schemas.contains(schema)) {
            return schema;
        }
        throw new AuthenticationServiceException("Invalid schema parameter found in authentication details");
    }
}
