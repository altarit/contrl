package com.altarit.contrl.client.api.cipher;

public class CipherStatic {

    public static class ClientHello {
        public String peerId;
        //public byte[] random;
        //public String publicKey;
    }

    public static class ServerHello {
        public String peerId;

        //public byte[] random;
        public byte[] keyExchange;
        //public String publicKey;
    }

    public static class ClientStartEncryption {
        public String peerId;
        public byte[] keyExchange;

        public byte[] vector;
        public byte[] cipherText;
    }

    public static class ServerStartEncryption {
        public String peerId;
        public String story;
        public String signature;

        public byte[] vector;
        public byte[] cipherText;
    }

    public static class EncryptedChatMessage {
        public String peerId;
        //public Class clazz;
        public String sclazz;
        public String type;

        public int suit;
        public byte[] vector;
        public byte[] cipherText;
    }
}
