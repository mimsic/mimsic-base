package com.github.mimsic.base.rabbit;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
        "com.github.mimsic.base.rabbit"
})
public class RabbitTestLauncher {

    public static void main(String[] args) {

        new SpringApplicationBuilder(RabbitTestLauncher.class)
                .properties("spring.config.location:classpath:/")
                .build().run(args);
    }
}
