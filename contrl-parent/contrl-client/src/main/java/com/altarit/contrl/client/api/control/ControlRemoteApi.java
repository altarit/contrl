package com.altarit.contrl.client.api.control;

import com.altarit.contrl.client.network.NetConnection;

public interface ControlRemoteApi {

    void sayWait(NetConnection connection, long millis);

    void runShellCmd(NetConnection connection, String command);

    void sendResponseFromCmd(NetConnection con, String text);
}
