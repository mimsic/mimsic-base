package com.github.mimsic.base.concurrency.handler;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ItemProcessor<T> {

    private final AtomicBoolean inProgress = new AtomicBoolean(false);
    private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

    private Lock lock;
    private ItemHandler<T> handler;

    private final Runnable runnable = () -> {

        T item;
        lock.lock();
        try {
            while (!inProgress.compareAndSet((item = queue.poll()) == null, false)) {
                try {
                    handler.process(item);
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

    public ItemProcessor(ItemHandler<T> handler, Lock lock) {
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
