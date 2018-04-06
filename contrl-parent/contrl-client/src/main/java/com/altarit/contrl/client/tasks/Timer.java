package com.altarit.contrl.client.tasks;

import com.altarit.contrl.client.console.CommandStore;
import com.altarit.contrl.client.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Timer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Timer.class);

    protected long millis;
    protected String command;

    public Timer(long millis, String command) {
        this.millis = millis;
        this.command = command;
    }

    public void run() {
        try {
            Thread.sleep(millis);
            CommandStore commandStore = SpringUtils.getBean(CommandStore.class);
            commandStore.execute(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
