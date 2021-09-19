package com.github.mimsic.base.ignite.config;

import org.apache.ignite.configuration.IgniteConfiguration;

public interface IgniteNetworkConfig {

    void accept(IgniteConfiguration igniteConfiguration, IgniteConfig igniteConfig);
}
