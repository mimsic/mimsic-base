package com.github.mimsic.base.rabbit.receiver;

import com.github.mimsic.base.common.utility.CharDelimiter;
import com.github.mimsic.base.rabbit.config.RabbitDestination;
import com.github.mimsic.base.rabbit.config.RabbitDestinationConfigurer;
import com.github.mimsic.base.rabbit.sender.RabbitSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ExtendWith(SpringExtension.class) already exists in @SpringBootTest
 */
@Disabled("Disabled until Broker is running")
@SpringBootTest
public class RabbitReceiveTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitReceiveTest.class);

    private CountDownLatch countDownLatch;
    private ExecutorService executorService;

    private int bucketLength;
    private int messageLength;
    private int messageNumber;

    private int receivedNumber;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitDestinationConfigurer destinationConfigurer;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private SimpleRabbitListenerContainerFactory containerFactory;

    public RabbitReceiveTest() {
    }

    @BeforeEach
    public void setUp() throws Exception {

        this.bucketLength = 5;
        this.messageLength = 50;
        this.messageNumber = 5000;
        this.receivedNumber = 0;

        int threadNumber = 2;
        this.countDownLatch = new CountDownLatch(messageNumber * threadNumber);
        this.executorService = Executors.newFixedThreadPool(threadNumber);
    }

    @AfterEach
    public void tearDown() throws Exception {

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
        }
    }

    @Test
    public void testListMessage() throws Exception {

        RabbitDestination rabbitDestination = destinationConfigurer.destination("qualifier1");

        RabbitReceiver<List<String>> rabbitReceiver = new RabbitReceiver<>(containerFactory, messageConverter, rabbitDestination);
        RabbitSender<List<String>> rabbitSender = new RabbitSender<>(rabbitTemplate, rabbitDestination);

        AutoCloseable closable = rabbitReceiver.listener(message -> {

            receivedNumber++;
            countDownLatch.countDown();
            return true;
        });

        StringBuilder stringBuilder = new StringBuilder(messageLength);
        for (int i = 0; i < messageLength; i++) {
            stringBuilder.append(CharDelimiter.DASH);
        }
        String message = stringBuilder.toString();
        Runnable runnable = () -> {

            List<String> bucket = new LinkedList<>();
            for (int i = 0; i < messageNumber; i++) {
                for (int j = 0; j < bucketLength; j++) {
                    bucket.add(message);
                }
                rabbitSender.send(bucket);
                bucket.clear();
                try {
                    Thread.sleep(1);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        executorService.execute(runnable);
        executorService.execute(runnable);
        try {
            if (!countDownLatch.await(60, TimeUnit.SECONDS)) {
                LOGGER.error("Send and receive failed");
                throw new TimeoutException();
            }
        } finally {
            closable.close();
        }
        LOGGER.info("Completed with received numbers {}", receivedNumber);
    }
}