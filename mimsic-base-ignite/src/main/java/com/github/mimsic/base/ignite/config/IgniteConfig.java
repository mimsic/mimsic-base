package com.github.mimsic.base.ignite.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "ignite-config")
@Profile("ignite")
@Getter
@Setter
@NoArgsConstructor
public class IgniteConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgniteConfig.class);

    private Network network;
    private String networkMode;
    private boolean clientMode;
    private boolean gridLogger;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info(toString());
    }

    @Override
    public String toString() {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append("Ignite Config: {").append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("network: ").append(network).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("networkMode: ").append(networkMode).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("clientMode: ").append(clientMode).append('\n');
        stringBuilder.append(StringUtils.leftPad("", 3)).append("gridLogger: ").append(gridLogger).append('\n');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Network {

        private AwsS3 awsS3;
        private Multicast multicast;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("awsS3: ").append(awsS3).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 6)).append("multicast: ").append(multicast);
            return stringBuilder.toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AwsS3 {

        private String bucketName;
        private String accessKey;
        private String secretKey;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9)).append("bucketName: ").append(bucketName).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9)).append("accessKey: ").append(accessKey);
            return stringBuilder.toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Multicast {

        private Communication communication;
        private Discovery discovery;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9)).append("communication: ").append(communication).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 9)).append("discovery: ").append(discovery);
            return stringBuilder.toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Communication {

        private int localPort;
        private int localPortRange;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 12)).append("localPort: ").append(localPort).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 12)).append("localPortRange: ").append(localPortRange);
            return stringBuilder.toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Discovery {

        private List<Address> addresses;
        private int localPort;
        private int localPortRange;
        private boolean shareMode;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 12)).append("addresses: ").append(addresses).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 12)).append("localPort: ").append(localPort).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 12)).append("localPortRange: ").append(localPortRange).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 12)).append("shareMode: ").append(shareMode);
            return stringBuilder.toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Address {

        private String ip;
        private String ports;

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('\n');
            stringBuilder.append(StringUtils.leftPad("", 15)).append("ip: ").append(ip).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 15)).append("port(s): ").append(ports).append('\n');
            stringBuilder.append(StringUtils.leftPad("", 12));
            return stringBuilder.toString();
        }
    }
}
