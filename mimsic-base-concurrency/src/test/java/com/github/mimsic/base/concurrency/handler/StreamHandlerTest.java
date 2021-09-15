package com.github.mimsic.base.concurrency.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

/**
 * @ExtendWith(SpringExtension.class) already exists in @SpringBootTest
 */
@SpringBootTest
public class StreamHandlerTest implements StreamHandler<Item> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamHandlerTest.class);

    private StreamProcessor<Item> streamProcessor;
    private CountDownLatch processorLatch;

    private long batchSize;
    private int itemNumber;
    private int threadNumber;

    @Autowired
    @Qualifier("StandardExecutor")
    private ThreadPoolExecutor standardThreadPoolExecutor;

    public StreamHandlerTest() {
        this.batchSize = 10;
        this.itemNumber = 100000;
        this.streamProcessor = new StreamProcessor<>(this, null, batchSize);
    }

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void testQueue() throws Exception {

        long timeStamp1 = System.nanoTime();
        processorLatch = new CountDownLatch(itemNumber);
        for (int i = 0; i < itemNumber; i++) {
            streamProcessor.queue(new Item(i));
        }
        if (!processorLatch.await(10, TimeUnit.SECONDS)) {
            LOGGER.error("Stream processor failed");
            throw new TimeoutException();
        }
        long timeStamp2 = System.nanoTime();
        LOGGER.info("Completed itemNumber {} with threadNumber {} and batchSize {}, in {} micro seconds",
                itemNumber, threadNumber, batchSize, (timeStamp2 - timeStamp1) / 1000);
    }

    @Override
    public void execute(Runnable task) {
        threadNumber++;
        standardThreadPoolExecutor.execute(task);
    }

    @Override
    public void generateLog(Exception ex) {
        LOGGER.info("", ex);
    }

    @Override
    public StreamProcessor<Item> getProcessor() {
        return streamProcessor;
    }

    @Override
    public void process(Stream<Item> stream) throws Exception {
        stream.forEach(item -> processorLatch.countDown());
    }
}