package com.altarit.contrl.client.network;

public interface NetworkListener {

    void connected(NetConnection connection);

    void disconnected(NetConnection connection);

    void received(NetConnection connection, Object object);
}
