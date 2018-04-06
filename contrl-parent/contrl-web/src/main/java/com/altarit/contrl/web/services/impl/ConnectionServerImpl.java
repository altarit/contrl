package com.altarit.contrl.web.services.impl;

import com.altarit.contrl.client.ContrlClient;
import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.web.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConnectionServerImpl implements ConnectionService {

    @Autowired
    private ContrlClient contrlClient;

    @Override
    public List<NetConnection> getConnections() {
        return new ArrayList<>();
    }

    @Override
    public void createConnection(String host, int port) {

    }
}
