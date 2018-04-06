package com.altarit.contrl.client.p2p;

import com.altarit.contrl.client.network.NetConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import java.security.PublicKey;

public class Peer {

    private static final Logger log = LoggerFactory.getLogger(Peer.class);

    private String id;

    private PublicKey publicKey;

    private SecretKeySpec secretKeySpec;

    private NetConnection connection;

    public Peer(String id) {
        this.id = id;
    }

    public Peer(String id, PublicKey publicKey) {
        this.id = id;
        this.publicKey = publicKey;
    }

    public String getId() {
        return id;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public SecretKeySpec getSecretKeySpec() {
        return secretKeySpec;
    }

    public void setSecretKeySpec(SecretKeySpec secretKeySpec) {
        this.secretKeySpec = secretKeySpec;
    }

    public NetConnection getConnection() {
        return connection;
    }

    public void setConnection(NetConnection connection) {
        this.connection = connection;
    }

    @Override
    public String toString() {
        return "peer:" + id;
    }
}
