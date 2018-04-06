package com.altarit.contrl.client.api.other;

import com.altarit.contrl.client.api.dispatcher.AbstractAction;

import java.util.ArrayList;

public class OtherActions {

    public static class UpdatePeersAction extends AbstractAction {
        public static final String CONNECTIONS_UPDATE_LIST_SUCCESS = "CONNECTIONS_UPDATE_LIST_SUCCESS";

        public UpdatePeersAction() {
            super(CONNECTIONS_UPDATE_LIST_SUCCESS);
        }

        public ArrayList<String> peers;

        public ArrayList<String> getPeers() {
            return peers;
        }

        public void setPeers(ArrayList<String> peers) {
            this.peers = peers;
        }
    }
}
