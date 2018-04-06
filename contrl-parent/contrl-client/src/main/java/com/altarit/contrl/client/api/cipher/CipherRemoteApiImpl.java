package com.altarit.contrl.client.api.cipher;

import com.altarit.contrl.client.network.NetConnection;
import com.altarit.contrl.client.p2p.Peer;
import com.altarit.contrl.client.p2p.PeerService;
import com.altarit.contrl.crypto.ciphers.CipherFacade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CipherRemoteApiImpl implements CipherRemoteApi {

    private static final Logger log = LoggerFactory.getLogger(CipherRemoteApiImpl.class);

    @Autowired
    private PeerService peerService;

    //@Autowired
    //private AsymmetricCipher asymmetricCipher;

    //@Autowired
    //private KeyAgreementCipher keyAgreementCipher;

    @Autowired
    private CipherFacade cipherFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void clientHello(NetConnection connection) {
        CipherStatic.ClientHello message = new CipherStatic.ClientHello();
        Peer me = peerService.getMe();
        message.peerId = me.getId();
        //message.random = asymmetricCipher.generateRandom();
        //message.publicKey = me.getPublicKey().getFormat();
        connection.send(message);
    }

    @Override
    public void serverHello(NetConnection connection) {
        Peer peer = peerService.getPeerByConnectionId(connection.getId());
        CipherStatic.ServerHello message = new CipherStatic.ServerHello();
        Peer me = peerService.getMe();
        message.peerId = me.getId();
        //message.random = asymmetricCipher.generateRandom();
        //message.publicKey = me.getPublicKey().getFormat();

        message.keyExchange = cipherFacade.generateKeyExchangePart(peer.getId());

        connection.send(message);
    }

    @Override
    public void clientStartEncryption(NetConnection connection, byte[] keyExchangePart) {
        Peer peer = peerService.getPeerByConnectionId(connection.getId());
        CipherStatic.ClientStartEncryption msg = new CipherStatic.ClientStartEncryption();
        msg.peerId = peerService.getMe().getId();

        //msg.keyExchange = cipherFacade.generateKeyExchangePart(peer.getId());
        String text = "Hi there!";
        msg.vector = cipherFacade.generateInitVector();
        msg.cipherText = cipherFacade.encrypt(peer.getId(), text, msg.vector);
        msg.keyExchange = keyExchangePart;
        connection.send(msg);
    }

    @Override
    public void serverStartEncryption(NetConnection connection) {
        Peer peer = peerService.getPeerByConnectionId(connection.getId());
        CipherStatic.ServerStartEncryption msg = new CipherStatic.ServerStartEncryption();
        msg.peerId = peerService.getMe().getId();

        String text = "Got it!";
        msg.vector = cipherFacade.generateInitVector();
        msg.cipherText = cipherFacade.encrypt(peer.getId(), text, msg.vector);
        connection.send(msg);
    }

    @Override
    public void sendEncryptedMessage(NetConnection connection, Object object) {
        try {
            String text = objectMapper.writeValueAsString(object);

            int suit = 0;

            if (suit == 0) {
                CipherStatic.EncryptedChatMessage message = new CipherStatic.EncryptedChatMessage();
                message.suit = 0;
                message.cipherText = text.getBytes();
                message.peerId = peerService.getMe().getId();
                //message.clazz = object.getClass();
                message.sclazz = object.getClass().getName();

                connection.send(message);
            } else if (suit == 1) {
                CipherStatic.EncryptedChatMessage message = new CipherStatic.EncryptedChatMessage();
                message.peerId = peerService.getMe().getId();
                Peer peer = peerService.getPeerByConnectionId(connection.getId());
                if (peer == null) {
                    log.error("Peer not found");
                    throw new RuntimeException("Peer not found");
                }
                message.suit = 1;
                message.vector = cipherFacade.generateInitVector();
                message.cipherText = cipherFacade.encrypt(peer.getId(), text, message.vector);
                //message.clazz = object.getClass();
                message.sclazz = object.getClass().getName();

                connection.send(message);

                /*try {
                    message.cipherText = asymmetricCipher.encryptText(text, peer.getPublicKey());
                } catch (Exception e) {
                    log.error("Exception at encryption", e);
                    throw new RuntimeException(e);
                }
                connection.send(message);  */
            }
        } catch (JsonProcessingException e) {
            log.error("Cannot stringify json " + e.getMessage(), e);
        }
    }
}
