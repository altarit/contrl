package com.altarit.contrl.console.adapters;

import com.altarit.contrl.client.console.ConsoleLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsoleLoggerImpl implements ConsoleLogger {

    private static final Logger log = LoggerFactory.getLogger(ConsoleLoggerImpl.class);

    @Override
    public void response(String s) {
        System.out.println(s);
    }

    @Override
    public void logObject(Object o) {
        System.out.println(o.toString());
    }
}
