package com.altarit.contrl.client.api.chat;

import com.altarit.contrl.client.api.cipher.AbstractRemoteApi;
import com.altarit.contrl.client.api.dispatcher.AbstractAction;
import com.altarit.contrl.client.api.dispatcher.ActionDispatcher;
import com.altarit.contrl.client.console.ConsoleLogger;
import com.altarit.contrl.client.network.NetConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatRemoteImpl extends AbstractRemoteApi implements ChatRemoteApi {

    private static final Logger log = LoggerFactory.getLogger(ChatRemoteImpl.class);

    @Autowired
    private ActionDispatcher actionDispatcher;

    @Override
    public void sendMessage(NetConnection connection, String s) {
        ChatStatic.ChatMessageAction chatMessage = new ChatStatic.ChatMessageAction(s);
        //actionDispatcher.dispatch(new AbstractAction("ACT_1"));
        //actionDispatcher.dispatch(new ChatStatic.ChatMessageAction(s));
        send(connection, chatMessage);
    }
}
