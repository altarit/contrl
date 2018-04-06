package com.altarit.contrl.client.api.netstat;

import com.altarit.contrl.client.network.NetConnection;

import java.util.ArrayList;
import java.util.List;

public interface NetContrlRemoteApi {

    void getAllPeers(NetConnection con, String requestId, ArrayList<String> path, int ttl);

    void sendPeers(NetConnection con, String nodeId, String requestId, ArrayList<String> path);
}
