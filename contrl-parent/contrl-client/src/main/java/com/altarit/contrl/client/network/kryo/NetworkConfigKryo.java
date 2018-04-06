package com.altarit.contrl.client.network.kryo;

import com.altarit.contrl.client.api.chat.ChatStatic;
import com.altarit.contrl.client.api.control.ControlStatic;
import com.altarit.contrl.client.api.netstat.NetContrlStatic;
import com.altarit.contrl.client.api.cipher.CipherStatic;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;

public class NetworkConfigKryo {

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(byte[].class);
        kryo.register(ArrayList.class);
        kryo.register(Class.class);

        // Chat
        kryo.register(String[].class);
        //kryo.register(ChatStatic.ChatMessage.class);

        // Control
        //kryo.register(ControlStatic.SayWait.class);
        //kryo.register(ControlStatic.CmdRequest.class);
        //kryo.register(ControlStatic.CmdResponse.class);

        // Secure Chat
        kryo.register(CipherStatic.ClientHello.class);
        kryo.register(CipherStatic.ServerHello.class);
        kryo.register(CipherStatic.ClientStartEncryption.class);
        kryo.register(CipherStatic.ServerStartEncryption.class);
        kryo.register(CipherStatic.EncryptedChatMessage.class);

        //NetContrl
        //kryo.register(NetContrlStatic.GetPeersRequest.class);
        //kryo.register(NetContrlStatic.GetPeersResponse.class);
    }
}
