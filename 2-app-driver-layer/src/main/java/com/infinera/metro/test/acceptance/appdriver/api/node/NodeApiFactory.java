package com.infinera.metro.test.acceptance.appdriver.api.node;

import com.infinera.metro.test.acceptance.appdriver.dnam.node.DnamNodeApi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Here we could add a key for next generation metro management application
 */
public enum NodeApiFactory {
    NODE_API_FACTORY;

    public enum NodeApiType {DNAM}

    final static Map<NodeApiType, Supplier<NodeApi>> map = new HashMap<>();
    static {
        map.put(NodeApiType.DNAM, DnamNodeApi::new);
    }
    public NodeApi getNodeApi(NodeApiType nodeApiType){
        Supplier<NodeApi> nodeApi = map.get(nodeApiType);
        if(nodeApi != null) {
            return nodeApi.get();
        }
        throw new IllegalArgumentException("No such NodeApi " + nodeApiType);
    }
}