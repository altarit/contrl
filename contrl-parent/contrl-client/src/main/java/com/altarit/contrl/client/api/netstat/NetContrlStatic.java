package com.altarit.contrl.client.api.netstat;

import com.altarit.contrl.client.api.dispatcher.AbstractAction;

import java.util.ArrayList;
import java.util.List;

public class NetContrlStatic {

    public static class GetPeersRequest extends AbstractAction {
        public static final String GET_NETWORK_RELATIONS_REQUEST = "GET_NETWORK_RELATIONS_REQUEST";

        public GetPeersRequest() {
            super(GET_NETWORK_RELATIONS_REQUEST);
        }

        public String requestId;

        public int ttl;

        public ArrayList<String> path;
    }

    public static class GetPeersResponse extends AbstractAction {
        public static final String GET_NETWORK_RELATIONS_SUCCESS = "GET_NETWORK_RELATIONS_SUCCESS";

        public GetPeersResponse() {
            super(GET_NETWORK_RELATIONS_SUCCESS);
        }

        public String requestId;

        public String nodeId;

        public ArrayList<String> path;

        public List<String> peers;
    }


}
