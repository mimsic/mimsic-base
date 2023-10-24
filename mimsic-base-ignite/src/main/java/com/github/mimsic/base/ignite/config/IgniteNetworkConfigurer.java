package com.github.mimsic.base.ignite.config;

import com.github.mimsic.base.common.utility.CharDelimiter;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
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

            IgniteConfig.Communication communication = igniteConfig.getNetworks().getMulticast().getCommunication();
            IgniteConfig.Discovery discovery = igniteConfig.getNetworks().getMulticast().getDiscovery();
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
