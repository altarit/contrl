package com.altarit.contrl.web.adapters;

import com.altarit.contrl.client.console.ConsoleLogger;
import com.altarit.contrl.web.services.CommandsService;
import com.altarit.contrl.web.wservices.WsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConsoleLoggerServerImpl implements ConsoleLogger {

    @Autowired
    private CommandsService commandsService;

    private Map<Class, Class> mapping = new ConcurrentHashMap<>();

    @Autowired
    private WsService wsService;

    @Override
    public void response(String s) {
        commandsService.log(s);
        wsService.sendConsoleOutput(s);
    }

    @Override
    public void logObject(Object o) {

    }
}
