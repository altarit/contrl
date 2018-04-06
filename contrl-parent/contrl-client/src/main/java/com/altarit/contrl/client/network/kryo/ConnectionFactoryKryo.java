package com.altarit.contrl.client.network.kryo;

import com.altarit.contrl.client.network.ConnectionFactory;
import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.network.NetworkListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;

@Component
public class ConnectionFactoryKryo implements ConnectionFactory {

    @Override
    public NetConnection createConnection(String host, int port, NetworkListener listener) throws IOException {
        Client nativeClient = new Client();
        nativeClient.start();
        NetworkConfigKryo.register(nativeClient);
        ConnectionKryo myConnection = new ConnectionKryo(nativeClient, new InetSocketAddress(host, port), listener);
        return myConnection;
    }

    public NetConnection wrapNativeConnection(Connection nativeConnection, NetworkListener listener) {
        ConnectionKryo myConnection = new ConnectionKryo(nativeConnection, nativeConnection.getRemoteAddressTCP(), listener);
        return myConnection;
    }
}
