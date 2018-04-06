package com.altarit.contrl.web.controllers.ws;

import com.altarit.contrl.web.controllers.entities.ConnectInputWsm;
import com.altarit.contrl.web.controllers.entities.ConsoleInputWsm;
import com.altarit.contrl.web.services.CommandsService;
import com.altarit.contrl.web.wservices.WsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WsController {

    private static final Logger log = LoggerFactory.getLogger(WsController.class);

    @Autowired
    private WsService wsService;

    @Autowired
    private CommandsService commandsService;

    /*@MessageMapping("/chat")
    @SendTo("/topic/message")
    public WsMessage sendWs(final WsMessage message) {
        log.debug("/chat received");
        final String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        return new WsMessage("/server/chat", time);
    }*/

    @MessageMapping("/console")
    public void getServerCommand(final ConsoleInputWsm message) {
        log.debug("/console received");
        commandsService.execute(message.getCommand());
    }

    @MessageMapping("/connections")
    public void getConnectionsList() {
        log.debug("/connections received");
        commandsService.execute("print");
        //List<String> peers = new ArrayList<>();
        //peers.add("peer1337");
        //peers.add("peer1338");
        //wsService.sendConnectionsList(peers);
    }

    @MessageMapping("/connect")
    public void connectTo(ConnectInputWsm message) {
        log.debug("/connect received");
        commandsService.execute("connect " + message.getAddress());
    }


}
