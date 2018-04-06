package com.altarit.contrl.crypto.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KeyExchangeStore {

    private static final Logger log = LoggerFactory.getLogger(KeyExchangeStore.class);

    private Map<String, KeyPair> map = new ConcurrentHashMap<>();

    public void putKeyPair(String peerId, KeyPair thisKeyPair) {
        if (map.get(peerId) != null) {
            log.warn("Key for {} already exists", peerId);
        }
        map.put(peerId, thisKeyPair);
    }

    public KeyPair getKeyPair(String peerId) {
        return map.get(peerId);
    }
}
