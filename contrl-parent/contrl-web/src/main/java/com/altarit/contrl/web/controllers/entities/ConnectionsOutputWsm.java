package com.altarit.contrl.web.controllers.entities;

import java.util.ArrayList;
import java.util.List;

public class ConnectionsOutputWsm {

    private String type;
    private List<String> peers;

    public ConnectionsOutputWsm() {

    }

    public ConnectionsOutputWsm(String type, List<String> peers) {
        this.peers = peers;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getPeers() {
        return peers;
    }

    public void setPeers(List<String> peers) {
        this.peers = peers;
    }
}
