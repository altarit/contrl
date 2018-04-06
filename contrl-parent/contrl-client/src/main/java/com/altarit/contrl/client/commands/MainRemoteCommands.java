package com.altarit.contrl.client.commands;

import com.altarit.contrl.client.api.chat.ChatRemoteApi;
import com.altarit.contrl.client.api.control.ControlRemoteApi;
import com.altarit.contrl.client.api.netstat.NetContrlRemoteApi;
import com.altarit.contrl.client.console.CommandStore;
import com.altarit.contrl.client.console.ConsoleLogger;
import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.network.Network;
import com.altarit.contrl.client.p2p.PeerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;

@Component
public class MainRemoteCommands {

    private static final Logger log = LoggerFactory.getLogger(MainRemoteCommands.class);

    @Autowired
    private CommandStore commandStore;

    @Autowired
    private ConsoleLogger consoleLogger;

    @Autowired
    private Network network;

    @Autowired
    private PeerService peerService;

    @Autowired
    private ChatRemoteApi chatRemoteApi;

    @Autowired
    private NetContrlRemoteApi netContrlRemoteApi;

    @Autowired
    private ControlRemoteApi controlRemoteApi;

    public void addCommands() {
        commandStore.on("send", (ars) -> {
            if (ars.length > 1) {
                try {
                    NetConnection connection = network.getConnectionById(network.getChosenConnection());
                    if (connection == null) {
                        consoleLogger.response("connection " + network.getChosenConnection() + " does not exists");
                        return;
                    }
                    if (!connection.isConnected()) {
                        consoleLogger.response("connection " + network.getChosenConnection() + " is not connected");
                        return;
                    }
                    chatRemoteApi.sendMessage(connection, String.join(" ", ars).substring(5));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                consoleLogger.response("command send <message>");
            }
        });
        Consumer<String[]> getPeers = (ars) -> {
            if (ars.length > 1) {
                try {
                    NetConnection connection = network.getConnectionById(network.getChosenConnection());
                    //cipherRemoteApi.sendEncryptedMessage(connection, String.join(" ", ars).substring(5));
                    //cipherRemoteApi.sendEncryptedMessage(connection);
                    Random random = new Random();
                    ArrayList<String> path = new ArrayList<String>();
                    path.add(peerService.getMe().getId());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    netContrlRemoteApi.getAllPeers(connection, "request#" + simpleDateFormat.format(new Date()), path, Integer.parseInt(ars[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                consoleLogger.response("command send <message>");
            }
        };
        commandStore.on("getp", getPeers);
        commandStore.on("say", (ars) -> {
            if (ars.length > 1) {
                try {
                    NetConnection connection = network.getConnectionById(network.getChosenConnection());
                    if (connection == null) {
                        consoleLogger.response("connection " + network.getChosenConnection() + " does not exists");
                        return;
                    }
                    if (!connection.isConnected()) {
                        consoleLogger.response("connection " + network.getChosenConnection() + " is not connected");
                        return;
                    }
                    switch (ars[1]) {
                        case "wait":
                            long millis = Long.parseLong(ars[2]);
                            controlRemoteApi.sayWait(connection, millis);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                consoleLogger.response("command say <command>");
            }
        });
        commandStore.on("cmd", (ars) -> {
            if (ars.length > 1) {
                try {
                    NetConnection connection = network.getConnectionById(network.getChosenConnection());
                    if (connection == null) {
                        consoleLogger.response("connection " + network.getChosenConnection() + " does not exists");
                        return;
                    }
                    if (!connection.isConnected()) {
                        consoleLogger.response("connection " + network.getChosenConnection() + " is not connected");
                        return;
                    }
                    controlRemoteApi.runShellCmd(connection, String.join(" ", ars).substring(4));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                consoleLogger.response("command: cmd <command>");
            }
        });
    }
}
