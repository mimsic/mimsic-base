package com.github.mimsic.base.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
        "com.github.mimsic.base.web"
})
public class WebTestLauncher {

    public static void main(String[] args) {

        new SpringApplicationBuilder(WebTestLauncher.class)
                .properties("spring.config.location:classpath:/")
                .build().run(args);
    }
}
