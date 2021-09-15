package com.github.mimsic.base.concurrency.handler;

import java.util.stream.Stream;

public interface StreamHandler<T> {

    void execute(Runnable task);

    void generateLog(Exception ex);

    StreamProcessor<T> getProcessor();

    void process(Stream<T> stream) throws Exception;
}
