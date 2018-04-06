package com.altarit.contrl.client.api.cipher;

import com.altarit.contrl.client.network.NetConnection;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractRemoteApi {

    @Autowired
    private CipherRemoteApi cipherRemoteApi;

    public void send(NetConnection connection, Object object) {
        cipherRemoteApi.sendEncryptedMessage(connection, object);
    }
}
