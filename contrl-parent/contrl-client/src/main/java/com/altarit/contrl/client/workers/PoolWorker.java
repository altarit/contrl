package com.altarit.contrl.client.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PoolWorker extends Thread {

    private static final Logger log = LoggerFactory.getLogger(PoolWorker.class);

    protected List<Runnable> tasksQueue;

    public PoolWorker(List tasksQueue) {
        this.tasksQueue = tasksQueue;
    }

    public void run() {
        Runnable r;
        while (!Thread.interrupted()) {
            synchronized (tasksQueue) {
                while (tasksQueue.isEmpty()) {
                    try {
                        tasksQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                r = tasksQueue.remove(0);
                log.debug("started task: {}", r);
            }

            try {
                r.run();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
}
