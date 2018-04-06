package com.altarit.contrl.client.commands;

import com.altarit.contrl.client.api.dispatcher.ActionDispatcher;
import com.altarit.contrl.client.api.other.OtherActions;
import com.altarit.contrl.client.console.CommandStore;
import com.altarit.contrl.client.console.ConsoleLogger;
import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.network.Network;
import com.altarit.contrl.client.p2p.Peer;
import com.altarit.contrl.client.p2p.PeerService;
import com.altarit.contrl.client.tasks.Timer;
import com.altarit.contrl.client.workers.WorkPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class PrintCommands {

    private static final Logger log = LoggerFactory.getLogger(PrintCommands.class);

    @Autowired
    private CommandStore commandStore;

    @Autowired
    private ConsoleLogger consoleLogger;

    @Autowired
    private Network network;

    @Autowired
    private PeerService peerService;

    @Autowired
    private WorkPool workPool;

    @Autowired
    private ActionDispatcher actionDispatcher;

    public void addCommands() {
        commandStore.on("hi", (ars) -> {
            String name = ars.length > 1 ? ars[1] : "pone";
            consoleLogger.response("Hello, " + name + "!");
        });

        Consumer<String[]> printConnections = (ars) -> {
            List<NetConnection> connections = network.getConnections();
            consoleLogger.response("Connections: " + connections.size());
            OtherActions.UpdatePeersAction result = new OtherActions.UpdatePeersAction();
            result.peers = new ArrayList<>();
            for (NetConnection connection : connections) {
                Peer peer = peerService.getPeerByConnectionId(connection.getId());
                result.peers.add(peer + " " + connection);
                consoleLogger.response(connection + " peerId=" + (peer == null ? null : peer.getId()));
            }
            actionDispatcher.dispatch(result);
        };
        commandStore.on("print", printConnections);


        commandStore.on("exit", (ars) -> {
            //consoleProcessor.close();
        });
        commandStore.on("timer", (ars) -> {
            long millis = ars.length > 1 ? Long.parseLong(ars[1]) : 1000;
            String[] innerArs = new String[ars.length - 2];
            System.arraycopy(ars, 2, innerArs, 0, innerArs.length);
            workPool.addTask(new Timer(millis, String.join(" ", innerArs)));
        });
    }


}
