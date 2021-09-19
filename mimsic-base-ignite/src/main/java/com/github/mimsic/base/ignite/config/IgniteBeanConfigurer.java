package com.github.mimsic.base.ignite.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.AtomicConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("ignite")
public class IgniteBeanConfigurer {

    @Bean(destroyMethod = "close")
    public Ignite ignite(
            IgniteConfig igniteConfig,
            IgniteNetworkConfig igniteNetworkConfig) {

        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setClientMode(igniteConfig.isClientMode());
        igniteConfiguration.setAtomicConfiguration(new AtomicConfiguration()
                .setBackups(1)
                .setCacheMode(CacheMode.PARTITIONED));
        if (igniteConfig.isGridLogger()) {
            igniteConfiguration.setGridLogger(new Slf4jLogger());
        }
        igniteNetworkConfig.accept(igniteConfiguration, igniteConfig);

        /* Required for ignite transactions
        igniteConfiguration.setPeerClassLoadingEnabled(true);
        igniteConfiguration.setTransactionConfiguration(transactionConfiguration());
        */

        Ignite ignite = Ignition.start(igniteConfiguration);
        return ignite;
    }

    /* Required for ignite transactions
    @Bean
    public SpringTransactionManager igniteTransactionManager() {
        return new SpringTransactionManager();
    }
    */

    /* Required for ignite transactions
    private TransactionConfiguration transactionConfiguration() {
        TransactionConfiguration configuration = new TransactionConfiguration();
        return configuration;
    }
    */
}
