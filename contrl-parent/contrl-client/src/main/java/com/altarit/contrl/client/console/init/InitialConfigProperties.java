package com.altarit.contrl.client.console.init;

import java.util.List;

public class InitialConfigProperties {

    public String id;

    public ServerProperties server;

    public List<String> onStartup;

    public KeyPairProps cipher;

    //public KeyPairProps signature;

    public List<PeerProps> peers;

    @Override
    public String toString() {
        return "InitialConfigProperties{\n" +
                "id='" + id + '\n' +
                "server=" + server + '\n' +
                "cipher=" + cipher + '\n' +
                "peers=" + peers + '\n' +
                '}';
    }

    public static class ServerProperties {
        public boolean enable;

        public int port;

        @Override
        public String toString() {
            return "{" + enable + ":" + port + "}";
        }
    }

    public static class KeyPairProps {
        public String publicKeyPath;
        public String privateKeyPath;

        @Override
        public String toString() {
            return "{paths=" + publicKeyPath + ";" + privateKeyPath+"}";
        }
    }

    public static class PeerProps {
        public String id;
        public String publicKeyPath;

        @Override
        public String toString() {
            return "id-" + id;
        }
    }
}
