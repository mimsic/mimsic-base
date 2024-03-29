package com.github.mimsic.base.concurrency;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
        "com.github.mimsic.base.concurrency"
})
public class ConcurrencyTestLauncher {

    public static void main(String[] args) {

        new SpringApplicationBuilder(ConcurrencyTestLauncher.class)
                .properties("spring.config.location:classpath:/")
                .build().run(args);
    }
}
