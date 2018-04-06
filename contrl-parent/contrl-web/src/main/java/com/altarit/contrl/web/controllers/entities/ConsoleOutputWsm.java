package com.altarit.contrl.web.controllers.entities;

public class ConsoleOutputWsm {

    private String type;
    private String text;

    public ConsoleOutputWsm(){
    }

    public ConsoleOutputWsm(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
