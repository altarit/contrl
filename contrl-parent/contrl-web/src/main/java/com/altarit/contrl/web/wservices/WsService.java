package com.altarit.contrl.web.wservices;

import com.altarit.contrl.client.api.dispatcher.AbstractAction;
import com.altarit.contrl.web.controllers.entities.ConnectionsOutputWsm;
import com.altarit.contrl.web.controllers.entities.ConsoleOutputWsm;
import com.altarit.contrl.web.services.CommandsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WsService {

    private static final Logger log = LoggerFactory.getLogger(WsService.class);

    private static final String MESSAGE_TOPIC = "/topic/message";

    @Autowired
    private CommandsService commandsService;

    @Autowired
    private SimpMessagingTemplate template;


    public void sendConsoleOutput(String s) {
        template.convertAndSend(MESSAGE_TOPIC, new ConsoleOutputWsm("CONSOLE_SERVER_OUTPUT_RESPONSE", s));
    }

    public void sendPreparedAction(AbstractAction s) {
        template.convertAndSend(MESSAGE_TOPIC, s);
    }

    public void sendConnectionsList(List<String> peers) {
        template.convertAndSend(MESSAGE_TOPIC, new ConnectionsOutputWsm("CONNECTIONS_UPDATE_LIST_SUCCESS", peers));
    }
}
