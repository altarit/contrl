package com.altarit.contrl.client.p2p;

import com.altarit.contrl.client.network.NetConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PeerService {

    private static final Logger log = LoggerFactory.getLogger(PeerService.class);

    private Map<String, Peer> peers = new ConcurrentHashMap<>();

    private Peer me;

    public void setMe(Peer peer) {
        me = peer;
    }

    public Peer getMe() {
        return me;
    }

    public void putPeer(Peer peer) {
        peers.put(peer.getId(), peer);
    }

    public Peer getPeer(String id) {
        return peers.get(id);
    }

    public Peer getPeerByConnectionId(int id) {
        for(Map.Entry<String, Peer> entry : peers.entrySet()) {
            NetConnection connection = entry.getValue().getConnection();
            if (connection != null && connection.getId() == id) {
                return entry.getValue();
            }
        }
        return null;
    }

    public List<String> getPeersIds() {
        return new ArrayList<String>(peers.keySet());
    }
}
