package com.altarit.contrl.client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class NetConnection {

    private static final Logger log = LoggerFactory.getLogger(NetConnection.class);

    protected static int instanceCounter = 0;
    protected final int id = ++instanceCounter;

    protected NetworkListener listener;
    protected InetSocketAddress remoteAddress;
    protected boolean isConnected = false;
    protected boolean isOutgoing = false;

    public void connect() throws IOException {
        throw new UnsupportedOperationException("Method 'MyConnection.connect' is not supported in superclass.");
    }

    public void reconnect() throws IOException {
        throw new UnsupportedOperationException("Method 'MyConnection.reconnect' is not supported in superclass.");
    }

    public void start() {
        throw new UnsupportedOperationException("Method 'MyConnection.start' is not supported in superclass.");
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int getId() {
        return id;
    }

    public InetSocketAddress getInetSocketAddress() {
        return remoteAddress;
    }

    public abstract void send(Object object);

    @Override
    public String toString() {
        return "Connection:{id:" + id +
                ", addr:" + remoteAddress.getHostName() + ":" + remoteAddress.getPort() +
                ", isConnected=" + isConnected +
                ", isOutgoing=" + isOutgoing + "}";
    }

}
