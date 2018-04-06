package com.altarit.contrl.web.services;

import com.altarit.contrl.client.network.NetConnection;

import java.util.List;

public interface ConnectionService {

    List<NetConnection> getConnections();

    void createConnection(String host, int port);
}
