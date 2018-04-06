package com.altarit.contrl.client.api.cipher;

import com.altarit.contrl.client.api.dispatcher.MessageDispatcherLocalApi;
import com.altarit.contrl.client.console.ConsoleLogger;
import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.p2p.Peer;
import com.altarit.contrl.client.p2p.PeerService;
import com.altarit.contrl.crypto.ciphers.CipherFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import com.altarit.contrl.client.api.cipher.CipherStatic.*;

@Component
public class CipherLocalApi {

    private static final Logger log = LoggerFactory.getLogger(CipherLocalApi.class);

    @Autowired
    private ConsoleLogger consoleLogger;

    @Autowired
    private PeerService peerService;

    //@Autowired
    //private AsymmetricCipher asymmetricCipher;

    @Autowired
    private CipherRemoteApi cipherRemoteApi;

    @Autowired
    private CipherFacade cipherFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageDispatcherLocalApi messageDispatcher;

    public void receive(NetConnection connection, Object object) {
        if (object instanceof ClientHello) {
            clientHello(connection, (ClientHello) object);
            return;
        }
        if (object instanceof CipherStatic.ServerHello) {
            serverHello(connection, (ServerHello) object);
            return;
        }
        if (object instanceof CipherStatic.ClientStartEncryption) {
            clientStartEncryption(connection, (ClientStartEncryption) object);
            return;
        }
        if (object instanceof CipherStatic.ServerStartEncryption) {
            serverStartEncryption(connection, (ServerStartEncryption) object);
            return;
        }
        if (object instanceof CipherStatic.EncryptedChatMessage) {
            message(connection, (EncryptedChatMessage)object);
            return;
        }
    }

    private void clientHello(NetConnection connection, ClientHello message) {
        Peer peer = peerService.getPeer(message.peerId);
        if (peer == null) {
            peer = new Peer(message.peerId);
            peerService.putPeer(peer);
        }
        peer.setConnection(connection);
        log.debug("client {} has been assigned to {}", message.peerId, connection.toString());

        Peer me = peerService.getMe();
        cipherRemoteApi.serverHello(connection);
    }


    private void serverHello(NetConnection connection, ServerHello message) {
        Peer peer = peerService.getPeer(message.peerId);
        if (peer == null) {
            peer = new Peer(message.peerId);
            peerService.putPeer(peer);
        }
        peer.setConnection(connection);
        log.debug("server {} has been assigned to {}", message.peerId, connection.toString());

        byte[] thisKeyExchangePart = cipherFacade.generateKeyExchangePart(message.peerId);
        cipherFacade.finishKeyExchange(message.peerId, message.keyExchange);
        cipherRemoteApi.clientStartEncryption(connection, thisKeyExchangePart);
    }


    private void clientStartEncryption(NetConnection connection, ClientStartEncryption message) {
        //Peer peer = peerService.getPeerByConnectionId(connection.getId());
        cipherFacade.finishKeyExchange(message.peerId, message.keyExchange);
        String text = cipherFacade.decrypt(message.peerId, message.cipherText, message.vector);
        log.debug("received at ClientStartEncryption: {}", text);
        cipherRemoteApi.serverStartEncryption(connection);
    }


    private void serverStartEncryption(NetConnection connection, ServerStartEncryption message) {
        String text = cipherFacade.decrypt(message.peerId, message.cipherText, message.vector);
        log.debug("received at ServerStartEncryption: {}", text);
    }

    private void message(NetConnection connection, EncryptedChatMessage message) {
        try {
            if (message.suit == 0) {
                String plainText = new String(message.cipherText);
                log.debug("as is='{}' clazz={}", plainText, message.sclazz);
                //Object parsed = objectMapper.readValue(plainText, message.clazz);
                Class obclazz = Class.forName(message.sclazz);
                Object parsed = objectMapper.readValue(plainText, obclazz);
                //log.debug("parsed={}", parsed);
                messageDispatcher.receive(connection, parsed, obclazz);
            } else if (message.suit == 1) {
                Peer peer = peerService.getPeer(message.peerId);
                //log.debug("encrypted: {}", message.cipherText);
                if (peer == null) {
                    log.error("Peer not found");
                    throw new RuntimeException("Peer not found");
                }
                if (peer.getConnection().getId() != connection.getId()) {
                    log.error("Peer.connection.id != connection.id");
                    throw new RuntimeException("Peer.connection.id != connection.id");
                }

                String plainText = cipherFacade.decrypt(message.peerId, message.cipherText, message.vector);
                log.debug("decrypted: {}", plainText);


                Class obclazz = Class.forName(message.sclazz);
                Object parsed = objectMapper.readValue(plainText, obclazz);
                messageDispatcher.receive(connection, parsed, obclazz);


                /*try {
                    String plainText = asymmetricCipher.decryptText(message.cipherText, secretStore.getPrivateDecryptKey());
                    log.debug("Decrypted: {}", plainText);
                } catch (Exception e) {
                    log.error("Exception at decryption", e);
                    throw new RuntimeException(e);
                }*/

                return;
            }
        } catch (IOException e) {
            log.error("error at parsing json " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error("class not found at parsing json " + e.getMessage(), e);
        }
    }

}
