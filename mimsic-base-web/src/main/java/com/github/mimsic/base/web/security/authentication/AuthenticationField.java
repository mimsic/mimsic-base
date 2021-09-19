package com.github.mimsic.base.web.security.authentication;

import com.github.mimsic.base.common.field.Field;

public class AuthenticationField {

    public static final Field accessToken = new Field("access_token");
    public static final Field authenticated = new Field("authenticated");
    public static final Field authorities = new Field("authorities");
    public static final Field credentials = new Field("credentials");
    public static final Field exp = new Field("exp");
    public static final Field jti = new Field("jti");
    public static final Field passwordReset = new Field("pwd_reset");
    public static final Field principal = new Field("principal");
    public static final Field refreshToken = new Field("refresh_token");
    public static final Field remoteAddress = new Field("remoteAddress");
    public static final Field schema = new Field("schema");
    public static final Field scope = new Field("scope");
    public static final Field tokenType = new Field("tokenType");
    public static final Field userId = new Field("user_id");
    public static final Field userName = new Field("user_name");
}
