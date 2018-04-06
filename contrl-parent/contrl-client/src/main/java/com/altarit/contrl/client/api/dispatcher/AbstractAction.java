package com.altarit.contrl.client.api.dispatcher;

public class AbstractAction {

    public String type;

    private AbstractAction() {
        System.out.println("new AbstractAction()");
    }

    public AbstractAction(String type) {
        System.out.println("new AbstractAction(String)");
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
