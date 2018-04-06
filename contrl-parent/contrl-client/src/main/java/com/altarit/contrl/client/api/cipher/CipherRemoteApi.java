package com.altarit.contrl.client.api.cipher;

import com.altarit.contrl.client.network.NetConnection;

public interface CipherRemoteApi {

    void clientHello(NetConnection connection);

    void serverHello(NetConnection connection);

    void clientStartEncryption(NetConnection connection, byte[] keyExchangePart);

    void serverStartEncryption(NetConnection connection);

    void sendEncryptedMessage(NetConnection connection, Object object);
}
