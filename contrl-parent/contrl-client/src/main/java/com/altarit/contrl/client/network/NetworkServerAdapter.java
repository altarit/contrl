package com.altarit.contrl.client.network;

import java.io.IOException;

public interface NetworkServerAdapter {

    void startServer(int port) throws IOException;

    void stopServer();

    void addServerListener(NetworkServerListener listener);

    void addClientListener(NetworkListener listener);
}
