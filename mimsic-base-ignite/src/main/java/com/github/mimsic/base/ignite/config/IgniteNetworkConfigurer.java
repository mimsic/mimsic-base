package com.github.mimsic.base.ignite.config;

import com.amazonaws.auth.BasicAWSCredentials;
import com.github.mimsic.base.common.utility.CharDelimiter;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.s3.TcpDiscoveryS3IpFinder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Configuration
@Profile("ignite")
public class IgniteNetworkConfigurer {

    @Bean
    @ConditionalOnProperty(prefix = "ignite-config", value = "network-mode", havingValue = "aws-s3")
    public IgniteNetworkConfig awsS3() {

        return (IgniteConfiguration igniteConfiguration, IgniteConfig igniteConfig) -> {

            IgniteConfig.AwsS3 awsS3 = igniteConfig.getNetwork().getAwsS3();
            igniteConfiguration
                    .setDiscoverySpi(new TcpDiscoverySpi()
                            .setIpFinder(new TcpDiscoveryS3IpFinder()
                                    .setAwsCredentials(new BasicAWSCredentials(
                                            awsS3.getAccessKey(),
                                            awsS3.getSecretKey()))
                                    .setBucketName(awsS3.getBucketName())));
        };
    }

    @Bean
    @ConditionalOnProperty(prefix = "ignite-config", value = "network-mode", havingValue = "kubernetes")
    public IgniteNetworkConfig kubernetes() {

        return (IgniteConfiguration igniteConfiguration, IgniteConfig igniteConfig) -> {

            igniteConfiguration
                    .setDiscoverySpi(new TcpDiscoverySpi()
                            .setIpFinder(new TcpDiscoveryKubernetesIpFinder()));
        };
    }

    @Bean
    @ConditionalOnProperty(prefix = "ignite-config", value = "network-mode", havingValue = "multicast")
    public IgniteNetworkConfig multicast() {

        return (IgniteConfiguration igniteConfiguration, IgniteConfig igniteConfig) -> {

            IgniteConfig.Communication communication = igniteConfig.getNetwork().getMulticast().getCommunication();
            IgniteConfig.Discovery discovery = igniteConfig.getNetwork().getMulticast().getDiscovery();
            igniteConfiguration
                    .setCommunicationSpi(new TcpCommunicationSpi()
                            .setLocalPort(communication.getLocalPort())
                            .setLocalPortRange(communication.getLocalPortRange()))
                    .setDiscoverySpi(new TcpDiscoverySpi()
                            .setLocalPort(discovery.getLocalPort())
                            .setLocalPortRange(discovery.getLocalPortRange())
                            .setIpFinder(new TcpDiscoveryMulticastIpFinder()
                                    .setShared(discovery.isShareMode())
                                    .setAddresses(discovery.getAddresses().stream()
                                            .map((IgniteConfig.Address address) -> new StringBuilder()
                                                    .append(address.getIp()).append(CharDelimiter.COLON)
                                                    .append(address.getPorts()).toString())
                                            .collect(Collectors.toCollection(ArrayList::new)))));
        };
    }
}
