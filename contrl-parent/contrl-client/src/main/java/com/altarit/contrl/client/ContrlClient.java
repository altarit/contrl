package com.altarit.contrl.client;

import com.altarit.contrl.client.api.chat.ChatLocalApi;
import com.altarit.contrl.client.api.chat.ChatStatic;
import com.altarit.contrl.client.api.cipher.CipherLocalApi;
import com.altarit.contrl.client.api.cipher.CipherRemoteApi;
import com.altarit.contrl.client.api.control.ControlLocalApi;
import com.altarit.contrl.client.api.control.ControlStatic;
import com.altarit.contrl.client.api.dispatcher.MessageDispatcherLocalApi;
import com.altarit.contrl.client.api.netstat.NetContrlLocalApi;
import com.altarit.contrl.client.api.netstat.NetContrlStatic;
import com.altarit.contrl.client.commands.MainRemoteCommands;
import com.altarit.contrl.client.commands.PrintCommands;
import com.altarit.contrl.client.commands.ServerCommands;
import com.altarit.contrl.client.console.CommandStore;
import com.altarit.contrl.client.console.ConsoleLogger;
import com.altarit.contrl.client.console.init.InitialArgumentProcessor;
import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.network.Network;
import com.altarit.contrl.client.network.NetworkListener;
import com.altarit.contrl.client.workers.WorkPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class ContrlClient {

    private static final Logger log = LoggerFactory.getLogger(ContrlClient.class);

    @Autowired
    private CommandStore commandStore;

    @Autowired
    private InitialArgumentProcessor initialArgumentProcessor;


    @Autowired
    private ChatLocalApi chatLocalApi;

    @Autowired
    private CipherRemoteApi cipherRemoteApi;

    @Autowired
    private CipherLocalApi schatLocalApi;


    @Autowired
    private ControlLocalApi controlLocalApi;

    @Autowired
    private WorkPool workPool;

    @Autowired
    private Network network;

    @Autowired
    private ConsoleLogger consoleLogger;

    @Autowired
    private NetContrlLocalApi netContrlLocalApi;

    @Autowired
    private MessageDispatcherLocalApi messageDispatcher;

    @Autowired
    PrintCommands printCommands;

    @Autowired
    ServerCommands serverCommands;

    @Autowired
    MainRemoteCommands mainRemoteCommands;

    String name;

    NetworkListener chatListener = new NetworkListener() {
        public void connected(NetConnection connection) {
            //chatRemoteApi.register(connection, name);
            //Peer me = peerService.getMe();
            cipherRemoteApi.clientHello(connection);
        }

        public void received(NetConnection connection, Object object) {
            //chatLocalApi.receive(connection, object);
            //controlLocalApi.receive(connection, object);
            schatLocalApi.receive(connection, object);
            //netContrlLocalApi.receive(connection, object);
        }

        public void disconnected(NetConnection connection) {
            log.debug("disconnected");
        }
    };

    public ContrlClient() {
        name = new Date().toString();
    }

    @PostConstruct
    public void init() {
        initConsoleProcessor();
        initialArgumentProcessor.init();
    }

    private void initConsoleProcessor() {

        printCommands.addCommands();
        serverCommands.addCommands(chatListener);
        mainRemoteCommands.addCommands();
    }

    private void registerMessages() {
        //messageDispatcher.register(ChatStatic.ChatMessage.class, chatLocalApi);
        messageDispatcher.register(NetContrlStatic.GetPeersRequest.class, netContrlLocalApi);
        messageDispatcher.register(NetContrlStatic.GetPeersResponse.class, netContrlLocalApi);
        messageDispatcher.register(ControlStatic.SayWait.class, controlLocalApi);
        messageDispatcher.register(ControlStatic.CmdRequest.class, controlLocalApi);
        messageDispatcher.register(ControlStatic.CmdResponse.class, controlLocalApi);
    }

    public void start(String[] args) throws Exception {
        //register meessages classes
        registerMessages();
        //start pool workers that accept tasks
        workPool.start();
        //set listeners
        network.setListener(chatListener);
        //process program arguments
        initialArgumentProcessor.process(args);
    }
}
