package com.altarit.contrl.client.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class WorkPool {

    private static final Logger log = LoggerFactory.getLogger(WorkPool.class);

    protected PoolWorker[] workers;
    protected List<Runnable> taskQueue = new LinkedList<>();

    public WorkPool(int size) {
        log.debug("workpool size: {}", size);
        workers = new PoolWorker[size];
    }

    public void start() {
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new PoolWorker(taskQueue);
            workers[i].start();
        }
    }

    public void addTask(Runnable r) {
        synchronized (taskQueue) {
            taskQueue.add(r);
            taskQueue.notify();
        }
    }
}
