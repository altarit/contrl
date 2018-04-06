package com.altarit.contrl.client.commands;

import com.altarit.contrl.client.console.CommandStore;
import com.altarit.contrl.client.console.ConsoleLogger;
import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.network.Network;
import com.altarit.contrl.client.network.NetworkListener;
import com.altarit.contrl.client.p2p.PeerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerCommands {

    private static final Logger log = LoggerFactory.getLogger(ServerCommands.class);

    @Autowired
    private CommandStore commandStore;

    @Autowired
    private ConsoleLogger consoleLogger;

    @Autowired
    private Network network;

    @Autowired
    private PeerService peerService;

    public void addCommands(NetworkListener chatListener) {
        commandStore.on("server", (ars) -> {
            if (ars.length > 1) {
                try {
                    if (ars[1].equals("start")) {
                        if (ars.length < 3) {
                            consoleLogger.response("command: server start <port>");
                            return;
                        }
                        int port = Integer.parseInt(ars[2]);
                        network.startServer(port);
                        consoleLogger.response("server has been started on " + port);
                        return;
                    }
                    consoleLogger.response("server: unknown option " + ars[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                consoleLogger.response("command: select <option>");
            }
        });
        commandStore.on("connect", (ars) -> {
            if (ars.length > 1) {
                try {
                    String host;
                    int port;
                    if (ars.length > 2) {
                        host = ars[1];
                        port = Integer.parseInt(ars[2]);
                    } else {
                        host = "localhost";
                        port = Integer.parseInt(ars[1]);
                    }
                    NetConnection connection = network.connect(host, port, chatListener);
                    network.setChosenConnection(connection.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                consoleLogger.response("command connect [<host>] <port>");
            }
        });
        commandStore.on("select", (ars) -> {
            if (ars.length > 1) {
                try {
                    int id = Integer.parseInt(ars[1]);
                    NetConnection connection = network.getConnectionById(id);
                    if (connection == null) {
                        consoleLogger.response("connection " + id + " does not exists");
                        return;
                    }
                    network.setChosenConnection(connection.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                consoleLogger.response("command: select <connection id>");
            }
        });

    }
}
