package com.altarit.contrl.client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class Network {

    private static final Logger log = LoggerFactory.getLogger(Network.class);

    @Autowired
    protected ConnectionFactory connectionFactory;

    @Autowired
    protected NetworkServerAdapter netServer;

    private List<NetConnection> connections = Collections.synchronizedList(new LinkedList<>());

    private int chosenConnection = 0;

    /**
     * Variable is stored here just for NetworkServerAdapter
     */
    //private NetworkListener networkListener;

    public Network() {
    }

    public NetConnection connect(String host, int port, NetworkListener listener) throws IOException {
        NetConnection oldConnection = findConnection(host, port);
        //TODO: implement reconnect
        if (oldConnection == null || true) {
            NetConnection connection = connectionFactory.createConnection(host, port, listener);
            connections.add(connection);
            connection.connect();
            return connection;
        } else {
            log.debug("found oldConnection {}", oldConnection);
            //oldConnection.reconnect();
            return oldConnection;
        }
    }

    private NetConnection findConnection(String host, int port) {
        InetSocketAddress inetAddress = new InetSocketAddress(host, port);
        NetConnection result = null;
        for(NetConnection connection : connections) {
            InetSocketAddress otherAddress = connection.getInetSocketAddress();
            if (otherAddress.equals(inetAddress)) {
                return connection;
            }
        }
        return result;
    }

    public List<NetConnection> getConnections() {
        return connections;
    }

    public NetConnection getConnectionById(int id) {
        return connections.stream().filter(con -> con.getId() == id).findFirst().get();
    }

    public void setListener(NetworkListener clientListener) {
        netServer.addClientListener(clientListener);
    }

    public void startServer(int port) throws IOException {
        netServer.addServerListener(new NetworkServerListener() {
            @Override
            public void accept(NetConnection con) {
                log.debug("accepted connection " + con.getId());
                connections.add(con);
            }
        });
        netServer.startServer(port);
    }

    public int getChosenConnection() {
        return chosenConnection;
    }

    public void setChosenConnection(int chosenConnection) {
        this.chosenConnection = chosenConnection;
    }
}
