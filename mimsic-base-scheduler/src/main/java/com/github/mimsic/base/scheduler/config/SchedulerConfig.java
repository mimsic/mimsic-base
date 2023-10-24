package com.github.mimsic.base.scheduler.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.TimeZone;

@Configuration
@ConfigurationProperties(prefix = "scheduler-config")
@Getter
@Setter
@NoArgsConstructor
public class SchedulerConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerConfigurer.class);

    private Set<SchedulerUnit> schedulers;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info(toString());
    }

    @Override
    public String toString() {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append("Scheduler Config: {").append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append(schedulers).append('\n');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SchedulerUnit {

        private String expression;
        private String status;
        private TimeZone timeZone;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("expression: ").append(expression).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("status: ").append(status).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("timeZone: ").append(timeZone.getID()).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 3));
            return stringBuilder.toString();
        }
    }
}
