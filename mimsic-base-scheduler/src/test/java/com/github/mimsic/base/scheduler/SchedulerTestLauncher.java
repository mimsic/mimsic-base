package com.github.mimsic.base.scheduler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
        "com.github.mimsic.base.concurrency",
        "com.github.mimsic.base.scheduler"
})
public class SchedulerTestLauncher {

    public static void main(String[] args) {

        new SpringApplicationBuilder(SchedulerTestLauncher.class)
                .properties("spring.config.location:classpath:/")
                .build().run(args);
    }
}
