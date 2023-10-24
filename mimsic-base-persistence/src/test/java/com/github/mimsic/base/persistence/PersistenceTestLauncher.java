package com.github.mimsic.base.persistence;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
        "com.github.mimsic.base.persistence"
})
public class PersistenceTestLauncher {

    public static void main(String[] args) {

        new SpringApplicationBuilder(PersistenceTestLauncher.class)
                .properties("spring.config.location:classpath:/")
                .build().run(args);
    }
}
