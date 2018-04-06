package com.altarit.contrl.client.network;

import java.io.IOException;

public interface ConnectionFactory {

    NetConnection createConnection(String host, int port, NetworkListener listener) throws IOException;
}
