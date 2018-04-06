package com.altarit.contrl.client.api.netstat;

import com.altarit.contrl.client.api.cipher.AbstractRemoteApi;
import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.p2p.PeerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NetContrlRemoteApiImpl extends AbstractRemoteApi implements NetContrlRemoteApi {

    private static final Logger log = LoggerFactory.getLogger(NetContrlRemoteApiImpl.class);

    @Autowired
    PeerService peerService;

    @Override
    public void getAllPeers(NetConnection con, String requestId, ArrayList<String> path, int ttl) {
        log.debug("getAllPeers requestId={} ttl={} path={}", requestId, ttl, path);
        NetContrlStatic.GetPeersRequest message = new NetContrlStatic.GetPeersRequest();
        message.path = path;
        message.ttl = ttl;
        message.requestId = requestId;
        //con.send(message);
        send(con, message);
    }

    @Override
    public void sendPeers(NetConnection con, String nodeId, String requestId, ArrayList<String> path) {
        NetContrlStatic.GetPeersResponse message = new NetContrlStatic.GetPeersResponse();
        message.peers = peerService.getPeersIds();
        log.debug("sendPeers requestId={} nodeId={} path={} peers={}", requestId, nodeId, path, message.peers);
        message.nodeId = nodeId;
        message.path = path;
        message.requestId = requestId;
        //con.send(message);
        send(con, message);
    }
}
