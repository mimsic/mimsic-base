package com.github.mimsic.base.concurrency.handler;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StreamProcessor<T> {

    private final AtomicBoolean inProgress = new AtomicBoolean(false);
    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

    private long size;
    private Lock lock;
    private StreamHandler<T> handler;

    private final Runnable runnable = () -> {

        lock.lock();
        try {
            while (!inProgress.compareAndSet(queue.isEmpty(), false)) {
                try {
                    handler.process(queue.stream().limit(size));
                } catch (Exception exception) {
                    handler.generateLog(exception);
                }
            }
        } catch (Exception ex) {
            inProgress.set(false);
            handler.generateLog(ex);
        } finally {
            lock.unlock();
        }
    };

    public StreamProcessor(StreamHandler<T> handler, Lock lock, long size) {
        this.size = size;
        this.lock = lock == null ? new ReentrantLock() : lock;
        this.handler = handler;
    }

    public void queue(T item) {
        try {
            queue.offer(item);
            if (inProgress.compareAndSet(false, true)) {
                handler.execute(runnable);
            }
        } catch (Exception ex) {
            inProgress.set(false);
            handler.generateLog(ex);
        }
    }
}
