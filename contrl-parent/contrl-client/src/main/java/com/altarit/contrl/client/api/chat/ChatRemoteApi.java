package com.altarit.contrl.client.api.chat;

import com.altarit.contrl.client.network.NetConnection;

public interface ChatRemoteApi {

    void sendMessage(NetConnection connection, String s);
}
