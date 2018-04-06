package com.altarit.contrl.web.services.impl;

import com.altarit.contrl.client.ContrlClient;
import com.altarit.contrl.client.console.CommandStore;
import com.altarit.contrl.web.services.CommandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

@Service
public class CommandsServiceImpl implements CommandsService {

    @Autowired
    private CommandStore commandStore;

    private List<String> outputList = Collections.synchronizedList(new LinkedList<String>());

    @Override
    public void log(String s) {
        outputList.add(s);
    }

    @Override
    public List<String> getLog() {
        return outputList;
    }

    @Override
    public void execute(String command) {
        commandStore.execute(command);
    }
}
