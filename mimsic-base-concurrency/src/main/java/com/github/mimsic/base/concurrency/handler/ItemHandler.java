package com.github.mimsic.base.concurrency.handler;

public interface ItemHandler<T> {

    void execute(Runnable task);

    void generateLog(Exception ex);

    ItemProcessor<T> getProcessor();

    void process(T item) throws Exception;
}
