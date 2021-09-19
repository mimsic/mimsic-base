package com.github.mimsic.base.web.common.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Keystore {

    private String alias;
    private String path;
    private String password;
    private String type;

    @Override
    public String toString() {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        stringBuilder.append("alias: ").append(alias);
        stringBuilder.append(", file: ").append(path);
        stringBuilder.append(", type: ").append(type);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
