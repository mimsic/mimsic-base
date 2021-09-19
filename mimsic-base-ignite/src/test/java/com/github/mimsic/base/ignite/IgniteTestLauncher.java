package com.github.mimsic.base.ignite;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.github.mimsic.base.ignite"
})
public class IgniteTestLauncher {

    public static void main(String[] args) {

        new SpringApplicationBuilder(IgniteTestLauncher.class)
                .properties("spring.config.location:classpath:/")
                .build().run(args);
    }
}
