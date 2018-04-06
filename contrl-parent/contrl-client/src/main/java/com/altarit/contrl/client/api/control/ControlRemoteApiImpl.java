package com.altarit.contrl.client.api.control;

import com.altarit.contrl.client.api.cipher.AbstractRemoteApi;
import com.altarit.contrl.client.network.NetConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ControlRemoteApiImpl extends AbstractRemoteApi implements ControlRemoteApi {

    public static final Logger log = LoggerFactory.getLogger(ControlRemoteApi.class);

    @Override
    public void sayWait(NetConnection connection, long millis) {
        ControlStatic.SayWait command = new ControlStatic.SayWait();
        command.millis = millis;
        send(connection, command);
    }

    @Override
    public void runShellCmd(NetConnection connection, String command) {
        ControlStatic.CmdRequest cmdRequest = new ControlStatic.CmdRequest(command);
        send(connection, cmdRequest);
    }

    @Override
    public void sendResponseFromCmd(NetConnection con, String text) {
        ControlStatic.CmdResponse message = new ControlStatic.CmdResponse(text);
        send(con, message);
    }
}
