package com.github.mimsic.base.scheduler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
@EnableScheduling
public class SchedulerConfigurer implements SchedulingConfigurer {

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private ScheduledTaskRegistrar scheduledTaskRegistrar;

    @Autowired
    public SchedulerConfigurer(
            @Qualifier("ScheduledExecutor") ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(scheduledThreadPoolExecutor);
        this.scheduledTaskRegistrar = scheduledTaskRegistrar;
    }

    public ScheduledTask registerCronTask(Runnable runnable, SchedulerConfig.SchedulerUnit schedulerUnit) {
        CronTrigger trigger = new CronTrigger(schedulerUnit.getExpression(), schedulerUnit.getTimeZone());
        CronTask task = new CronTask(runnable, trigger);
        scheduledTaskRegistrar.addCronTask(task);
        return scheduledTaskRegistrar.scheduleCronTask(task);
    }
}
