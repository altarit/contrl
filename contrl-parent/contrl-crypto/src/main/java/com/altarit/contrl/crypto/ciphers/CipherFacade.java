package com.altarit.contrl.crypto.ciphers;

import com.altarit.contrl.crypto.store.KeyExchangeStore;
import com.altarit.contrl.crypto.store.SessionKeysStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

@Component
public class CipherFacade {

    private static final Logger log = LoggerFactory.getLogger(CipherFacade.class);

    @Autowired
    private SymmetricCipher symmetricCipher;

    @Autowired
    private KeyAgreementCipher keyAgreementCipher;

    @Autowired
    private KeyExchangeStore keyExchangeStore;

    @Autowired
    private SessionKeysStore sessionKeysStore;

    @Autowired
    private HashCipher hashCipher;

    private SecureRandom random = new SecureRandom();

    public byte[] generateKeyExchangePart(String peerId) {
        KeyPair thisKeyPair = keyAgreementCipher.generateDHKeys();
        keyExchangeStore.putKeyPair(peerId, thisKeyPair);
        return thisKeyPair.getPublic().getEncoded();
    }

    public void finishKeyExchange(String peerId, byte[] otherPart) {
        PublicKey otherPublic = keyAgreementCipher.getPublicKey(otherPart);
        KeyPair thisKeyPair = keyExchangeStore.getKeyPair(peerId);
        PrivateKey thisPrivate = thisKeyPair.getPrivate();
        PublicKey thisPublic = thisKeyPair.getPublic();
        byte[] preMaster = keyAgreementCipher.generatePreMaster(thisPrivate, otherPublic);
        byte[] masterKey = keyAgreementCipher.generateMasterSecret(preMaster, thisPublic.getEncoded(), otherPublic.getEncoded());
        log.debug("aPublic({}) aPrivate({}) otherPart({}) bPublic({})",
                thisPublic.getEncoded().length, thisPrivate.getEncoded().length, otherPart.length, otherPublic.getEncoded().length);
        log.debug("preMaster({}) masterKey({})", preMaster.length, masterKey.length);

        //TODO: generate client->server, server->client and signature keys
        SecretKey secretKey = hashCipher.getSecretKey(masterKey, "secret".getBytes());
        log.debug("secretKey({}) = {}", secretKey.getEncoded().length, secretKey.getFormat());
        sessionKeysStore.putSessionData(peerId, secretKey);
    }

    public byte[] generateInitVector() {
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

    public byte[] encrypt(String peerId, String text, byte[] initVector) {
        SecretKey key = sessionKeysStore.getSecretKey(peerId);
        return symmetricCipher.encrypt(text, key, initVector);
    }

    public String decrypt(String peerId, byte[] cipherText, byte[] initVector) {
        SecretKey key = sessionKeysStore.getSecretKey(peerId);
        return symmetricCipher.decrypt(cipherText, key, initVector);
    }
}
