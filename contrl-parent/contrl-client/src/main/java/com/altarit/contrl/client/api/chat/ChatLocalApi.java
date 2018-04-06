package com.altarit.contrl.client.api.chat;

import com.altarit.contrl.client.api.dispatcher.ActionDispatcher;
import com.altarit.contrl.client.console.ConsoleLogger;
import com.altarit.contrl.client.network.LocalApi;
import com.altarit.contrl.client.network.NetConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.altarit.contrl.client.api.chat.ChatStatic.*;

@Component
public class ChatLocalApi implements LocalApi {

    private static final Logger log = LoggerFactory.getLogger(ChatLocalApi.class);

    @Autowired
    private ConsoleLogger consoleLogger;

    public void receive(NetConnection connection, Object object) {
        log.debug("object: {}", object);
        if (object instanceof ChatMessageAction) {
            receiveMessage((ChatMessageAction) object);
            return;
        }
    }

    private void receiveMessage(ChatMessageAction message) {
        consoleLogger.response(message.getMessage());
    }
}
