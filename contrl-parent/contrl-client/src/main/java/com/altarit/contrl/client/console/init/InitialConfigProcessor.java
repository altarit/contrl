package com.altarit.contrl.client.console.init;

import com.altarit.contrl.client.console.CommandStore;
import com.altarit.contrl.client.console.ConsoleLogger;
import com.altarit.contrl.client.network.Network;
import com.altarit.contrl.client.p2p.Peer;
import com.altarit.contrl.client.p2p.PeerService;
import com.altarit.contrl.crypto.ciphers.AsymmetricCipher;
import com.altarit.contrl.crypto.store.PublicKeyStore;
import com.altarit.contrl.crypto.store.SecretStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class InitialConfigProcessor {

    private static final Logger log = LoggerFactory.getLogger(InitialConfigProcessor.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsoleLogger consoleLogger;

    @Autowired
    private Network network;

    @Autowired
    private CommandStore commandStore;

    @Autowired
    private SecretStore secretStore;

    @Autowired
    private AsymmetricCipher asymmetricCipher;

    @Autowired
    private PublicKeyStore publicKeyStore;

    @Autowired
    private PeerService peerService;

    public void initByJson(String json) throws IOException, GeneralSecurityException {
        InitialConfigProperties config = objectMapper.readValue(json, InitialConfigProperties.class);
        log.debug("config: {}", config);

        Peer me = new Peer(config.id, secretStore.getPublicEncryptKey());
        peerService.setMe(me);

        if (config.server != null && config.server.enable) {
            log.debug("starting server on {}", config.server.port);
            network.startServer(config.server.port);
        }

        if (config.onStartup != null && !config.onStartup.isEmpty()) {
            config.onStartup.forEach(commandStore::execute);
        }

        if (config.cipher != null) {
            if (config.cipher.publicKeyPath != null) {
                PublicKey publicKey = asymmetricCipher.readPublicFromFile(config.cipher.publicKeyPath);
                secretStore.setPublicEncryptKey(publicKey);
            }
            if (config.cipher.privateKeyPath != null) {
                PrivateKey privateKey = asymmetricCipher.readPrivateFromFile(config.cipher.privateKeyPath);
                secretStore.setPrivateDecryptKey(privateKey);
            }
        }


        if (config.peers != null) {
            for (InitialConfigProperties.PeerProps peerProps : config.peers) {
                if (peerProps.publicKeyPath != null) {
                    PublicKey key = asymmetricCipher.readPublicFromFile(peerProps.publicKeyPath);
                    publicKeyStore.put(peerProps.id, key);

                    Peer peer = new Peer(peerProps.id, key);
                    peerService.putPeer(peer);
                }
            }
        }
    }
}
