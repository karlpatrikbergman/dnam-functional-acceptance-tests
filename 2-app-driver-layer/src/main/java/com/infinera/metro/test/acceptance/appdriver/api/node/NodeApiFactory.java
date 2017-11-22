package com.infinera.metro.test.acceptance.appdriver.api.node;

import com.infinera.metro.test.acceptance.appdriver.dnam.node.DnamNodeApi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum NodeApiFactory {
    NODE_API_FACTORY;

    final static Map<NodeApi.Type, Supplier<NodeApi>> map = new HashMap<>();
    static {
        map.put(NodeApi.Type.DNAM, DnamNodeApi::new);
    }
    public NodeApi getNodeApi(NodeApi.Type nodeApiType){
        Supplier<NodeApi> nodeApi = map.get(nodeApiType);
        if(nodeApi != null) {
            return nodeApi.get();
        }
        throw new IllegalArgumentException("No such NodeApi " + nodeApiType);
    }
}