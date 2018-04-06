package com.altarit.contrl.web.controllers.entities;

public class ConsoleInputWsm {

    private String type;
    private String command;

    public ConsoleInputWsm() {
    }

    public ConsoleInputWsm(String type, String command) {
        this.type = type;
        this.command = command;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
