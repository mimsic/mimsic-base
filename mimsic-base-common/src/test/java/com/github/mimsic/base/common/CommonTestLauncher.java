package com.github.mimsic.base.common;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
        "com.github.mimsic.base.common"
})
public class CommonTestLauncher {

    public static void main(String[] args) {

        new SpringApplicationBuilder(CommonTestLauncher.class)
                .properties("spring.config.location:classpath:/")
                .build().run(args);
    }
}
