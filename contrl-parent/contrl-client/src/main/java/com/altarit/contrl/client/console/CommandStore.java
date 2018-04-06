package com.altarit.contrl.client.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class CommandStore {

    private static final Logger log = LoggerFactory.getLogger(CommandStore.class);

    @Autowired
    private ConsoleLogger consoleLogger;

    protected Map<String, Consumer<String[]>> commands = new HashMap<>();

    public CommandStore() {
        commands.put("exit", (args) -> {
            throw new RuntimeException();
        });
        commands.put("?", (args) -> {
            consoleLogger.response(commands.keySet().toString());
        });
    }

    public void on(String s, Consumer<String[]> f) {
        commands.put(s, f);
    }

    public void execute(String command) {
        String[] arr = command.split(" +");
        execute(arr[0], arr);
    }

    public void execute(String command, String[] args) {
        Consumer<String[]> f = commands.get(command);
        if (f == null) {
            consoleLogger.response("Unknown command: " + command);
            return;
        }
        f.accept(args);
    }
}
