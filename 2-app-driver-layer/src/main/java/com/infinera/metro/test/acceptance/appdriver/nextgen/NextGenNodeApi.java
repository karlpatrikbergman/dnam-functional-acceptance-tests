package com.infinera.metro.test.acceptance.appdriver.nextgen;

import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import lombok.Value;

@Value
public class NextGenNodeApi implements NodeApi {
    private final RemoteServiceAccessData remoteServiceAccessData;

    @Override
    public void addNode(Node node) {
        //Invoke REST service or other depending on implementation of next generation metro management application
    }

    @Override
    public Node getNode(Node node) {
        return null;
    }

    @Override
    public void deleteNode(Node node) {

    }

    @Override
    public boolean assertNodeNotAdded(Node node) {
        return false;
    }
}
