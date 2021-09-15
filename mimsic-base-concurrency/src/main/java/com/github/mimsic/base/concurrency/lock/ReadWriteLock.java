package com.github.mimsic.base.concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public ReadWriteLock() {
    }

    public void grabReadLock() {
        readLock.lock();
    }

    public void grabWriteLock() {
        writeLock.lock();
    }

    public void releaseReadLock() {
        readLock.unlock();
    }

    public void releaseWriteLock() {
        writeLock.unlock();
    }
}
