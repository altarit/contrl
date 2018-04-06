package com.altarit.contrl.client.api.netstat;

import com.altarit.contrl.client.network.LocalApi;
import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.network.Network;
import com.altarit.contrl.client.p2p.Peer;
import com.altarit.contrl.client.p2p.PeerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.altarit.contrl.client.api.netstat.NetContrlStatic.*;

import java.util.List;

@Service
public class NetContrlLocalApi implements LocalApi {

    private static final Logger log = LoggerFactory.getLogger(NetContrlLocalApi.class);

    @Autowired
    NetContrlRemoteApi netContrlRemoteApi;

    @Autowired
    Network network;

    @Autowired
    PeerService peerService;

    @Override
    public void receive(NetConnection con, Object object) {
        if (object instanceof GetPeersRequest) {
            getPeersRequest(con, (NetContrlStatic.GetPeersRequest) object);
            return;
        }
        if (object instanceof GetPeersResponse) {
            getPeerResponse(con, (GetPeersResponse) object);
            return;
        }
    }

    private void getPeersRequest(NetConnection con, GetPeersRequest message) {
        Peer peer = peerService.getPeerByConnectionId(con.getId());
        log.debug("received GetPeersRequest {} from {} ttl={}", message.requestId, peer.getId(), message.ttl);
        log.debug("path={}", message.path);
            /*synchronized (oldRequests) {
                if (oldRequests.contains(message.requestId)) {
                    return;
                } else {
                    oldRequests.add(message.requestId);
                }
            }    */
        netContrlRemoteApi.sendPeers(con, peerService.getMe().getId(), message.requestId, message.path);

        if (message.ttl <= 0) {
            return;
        }
        List<NetConnection> nextCons = network.getConnections();
        message.path.add(peerService.getMe().getId());
        for (NetConnection nextCon : nextCons) {
            if (!nextCon.isConnected()) {
                continue;
            }
            Peer nextPeer = peerService.getPeerByConnectionId(nextCon.getId());
            if (message.path.contains(nextPeer.getId())) {
                continue;
            }
            netContrlRemoteApi.getAllPeers(nextCon, message.requestId, message.path, message.ttl - 1);
        }
    }

    private void getPeerResponse(NetConnection con, GetPeersResponse message) {
        Peer peer = peerService.getPeerByConnectionId(con.getId());
        log.debug("received GetPeersResponse:{} node:{} from:{}", message.requestId, message.nodeId, peer.getId());
        log.debug("path={}", message.path);
        if (!message.path.get(0).equals(peerService.getMe().getId())) {
            log.debug("transit!");
            message.path.remove(message.path.size() - 1);
            Peer nextPeer = peerService.getPeer(message.path.get(message.path.size() - 1));

            netContrlRemoteApi.sendPeers(nextPeer.getConnection(), message.nodeId, message.requestId, message.path);
        } else {
            log.debug("destination!");
        }
        for (String s : message.peers) {
            log.debug(" -peer: {}", s);
        }
    }
}
