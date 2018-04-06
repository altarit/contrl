package com.altarit.contrl.client.network.kryo;

import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.network.NetworkListener;
import com.altarit.contrl.client.network.NetworkServerAdapter;
import com.altarit.contrl.client.network.NetworkServerListener;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NetworkServerAdapterKryo implements NetworkServerAdapter {

    public static final Logger log = LoggerFactory.getLogger(NetworkServerAdapterKryo.class);

    private Server nativeServer;
    private NetworkServerListener serverListener;
    private NetworkListener listener;

    @Autowired
    private ConnectionFactoryKryo connectionFactory;

    public NetworkServerAdapterKryo() {
        nativeServer = new Server() {
            protected Connection newConnection() {
                return new ChatConnection();
            }
        };
        NetworkConfigKryo.register(nativeServer);
    }

    @Override
    public void startServer(int port) throws IOException {
        nativeServer.bind(port);
        nativeServer.addListener(new Listener() {
            @Override
            public void connected(Connection c) {
                log.debug("server: connected: {}", c.getID());
                NetConnection netConnection = connectionFactory.wrapNativeConnection(c, listener);
                netConnection.start();
                serverListener.accept(netConnection);
            }

            @Override
            public void received(Connection c, Object object) {
                log.trace("server: received: {} - {}", c.getID(), object);
            }

            @Override
            public void disconnected(Connection c) {

            }
        });
        nativeServer.start();
    }

    @Override
    public void stopServer() {

    }

    @Override
    public void addServerListener(NetworkServerListener listener) {
        this.serverListener = listener;
    }

    @Override
    public void addClientListener(NetworkListener listener) {
        this.listener = listener;
    }

    static class ChatConnection extends Connection {
        public String name;
    }
}
