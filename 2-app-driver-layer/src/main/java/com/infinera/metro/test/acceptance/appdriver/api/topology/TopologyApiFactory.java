package com.infinera.metro.test.acceptance.appdriver.api.topology;

import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.dnam.topology.DnamTopologyApi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * NEXT_GEN_APP is an example of how we could add/replace implementation of NodeApi
 * Here we assume that implementation will need an ipaddress and a port, but protocol
 * is not specified.
 */
public enum TopologyApiFactory {
    TOPOLOGY_API_FACTORY;

    public enum TopologyApiType  {DNAM }
    final static Map<TopologyApiType, Function<RemoteServiceAccessData, TopologyApi>> map = new HashMap<>();

    static {
        map.put(TopologyApiType.DNAM, DnamTopologyApi::new);

    }

    public TopologyApi getTopologyApi(TopologyApiType topologyApiType, RemoteServiceAccessData remoteServiceAccessData){
        Function<RemoteServiceAccessData, TopologyApi> nodeApi = map.get(topologyApiType);
        if(nodeApi != null) {
            return nodeApi.apply(remoteServiceAccessData);
        }
        throw new IllegalArgumentException("No such TopologyApi " + topologyApiType);
    }
}