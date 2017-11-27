package com.infinera.metro.test.acceptance.appdriver.api.node;

import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.dnam.node.DnamNodeApi;
import com.infinera.metro.test.acceptance.appdriver.nextgen.node.NextGenNodeApi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * NEXT_GEN_APP is an example of how we could add/replace implementation of NodeApi
 * Here we assume that implementation will need an ipaddress and a port, but protocol
 * is not specified.
 */
public enum NodeApiFactory {
    NODE_API_FACTORY;

    public enum NodeApiType {DNAM, NEXT_GEN_APP}
    final static Map<NodeApiType, Function<RemoteServiceAccessData, NodeApi>> map = new HashMap<>();

    static {
        map.put(NodeApiType.DNAM, DnamNodeApi::new);
        map.put(NodeApiType.NEXT_GEN_APP, NextGenNodeApi::new);
    }

    public NodeApi getNodeApi(NodeApiType nodeApiType, RemoteServiceAccessData remoteServiceAccessData){
        Function<RemoteServiceAccessData, NodeApi> nodeApi = map.get(nodeApiType);
        if(nodeApi != null) {
            return nodeApi.apply(remoteServiceAccessData);
        }
        throw new IllegalArgumentException("No such NodeApi " + nodeApiType);
    }
}