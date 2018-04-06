package com.altarit.contrl.client.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class ConsoleProcessor {

    private static final Logger log = LoggerFactory.getLogger(ConsoleProcessor.class);

    @Autowired
    protected CommandStore commandStore;

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public void start() {
        try {
            while (!Thread.interrupted()) {
                commandStore.execute(br.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        Thread.currentThread().interrupt();
    }


}
