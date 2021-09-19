package com.github.mimsic.base.web.security.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ExtendWith(SpringExtension.class) already exists in @SpringBootTest
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EncoderConfigurerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncoderConfigurerTest.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPasswordEncoder() {

        String rawPassword = "1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        LOGGER.info("raw password: {} -> encoded password: {}", rawPassword, encodedPassword);

        Assertions.assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}