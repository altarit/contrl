package com.altarit.contrl.web.controllers.entities;

public class ConnectInputWsm {

    private String type;
    private String address;

    public ConnectInputWsm() {
    }

    public ConnectInputWsm(String type, String address) {
        this.type = type;
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
