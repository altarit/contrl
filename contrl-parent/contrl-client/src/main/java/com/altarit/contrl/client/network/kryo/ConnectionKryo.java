package com.altarit.contrl.client.network.kryo;

import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.network.NetworkListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

//@Component
//@Scope("prototype")
public class ConnectionKryo extends NetConnection {

    private static final Logger log = LoggerFactory.getLogger(ConnectionKryo.class);

    private Connection nativeConnection;

    public ConnectionKryo(Connection nativeConnection, InetSocketAddress addr, NetworkListener listener) {
        this.remoteAddress = addr;
        this.nativeConnection = nativeConnection;
        this.listener = listener;
    }

    public void connect() throws IOException {
        if (isConnected) {
            throw new IllegalStateException("Connection " + id + " already connected.");
        }
        if (nativeConnection instanceof Client) {
            isOutgoing = true;
            Client nativeClient = (Client) nativeConnection;
            startListeners();
            nativeClient.connect(5000, remoteAddress.getHostName(), remoteAddress.getPort());
        } else {
            throw new UnsupportedOperationException("Method 'connect' is applied only for isOutgoing connections");
        }
    }

    public void reconnect() throws IOException {
        if (nativeConnection instanceof Client) {
            isOutgoing = true;
            Client nativeClient = (Client) nativeConnection;
            startListeners();
            nativeClient.reconnect();
        } else {
            throw new UnsupportedOperationException("Method 'connect' is allowed only for outgoing connections");
        }
    }

    public void start() {
        if (isConnected) {
            throw new IllegalStateException("Connection " + id + " already connected.");
        }
        startListeners();
        isConnected = true;
        isOutgoing = false;
    }

    private void startListeners() {
        nativeConnection.addListener(new Listener() {
            public void connected(Connection connection) {
                listener.connected(ConnectionKryo.this);
                isConnected = true;
            }

            public void received(Connection connection, Object object) {
                listener.received(ConnectionKryo.this, object);
            }

            public void disconnected(Connection connection) {
                isConnected = false;
                listener.disconnected(ConnectionKryo.this);
            }
        });
    }

    public void send(Object object) {
        nativeConnection.sendTCP(object);
    }

    public class ClientNetworkListener implements NetworkListener {
        @Override
        public void connected(NetConnection connection) {
            isConnected = true;
            log.debug("connected");
            listener.connected(connection);
        }

        @Override
        public void disconnected(NetConnection connection) {
            listener.disconnected(connection);
        }

        @Override
        public void received(NetConnection connection, Object object) {
            listener.received(connection, object);
        }
    }

}
