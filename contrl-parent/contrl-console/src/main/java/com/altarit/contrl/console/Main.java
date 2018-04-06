package com.altarit.contrl.console;

import com.altarit.contrl.client.ContrlClient;
import com.altarit.contrl.client.console.ConsoleProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Autowired
    ContrlClient contrlClient;

    @Autowired
    ConsoleProcessor consoleProcessor;

    public static void main(String[] args) throws Exception {
        log.debug("console client started");
        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.altarit.contrl.console");
        Main main = ctx.getBean(Main.class);
        main.contrlClient.start(args);
        //start console reader and block current thread
        main.consoleProcessor.start();
    }
}
