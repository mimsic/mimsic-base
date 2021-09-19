package com.github.mimsic.base.web.security.authentication;

import com.github.mimsic.base.common.utility.CharDelimiter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Map;

public class UserExtended extends User {

    private final Map<String, Object> claims;

    public UserExtended(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> claims) {

        super(username, password, authorities);
        this.claims = claims;
    }

    public UserExtended(
            String username,
            String password,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> claims) {

        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.claims = claims;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append(CharDelimiter.SEMICOLON);
        stringBuilder.append(CharDelimiter.SPACE);
        stringBuilder.append("claims: ");
        stringBuilder.append(claims);
        return stringBuilder.toString();
    }
}