package com.infinera.metro.test.acceptance.appdriver.nextgen;

import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import lombok.Value;

@Value
public class NextGenNodeApi implements NodeApi {
    private final RemoteServiceAccessData remoteServiceAccessData;

    @Override
    public void addNode(String ipAddress) {
        //Invoke REST service or other depending on implementation of next generation metro management application
    }
}
