package com.infinera.metro.test.acceptance.appdriver.dnam.node;

import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import com.infinera.metro.test.acceptance.appdriver.dnam.DnamRmiClient;
import com.infinera.metro.test.acceptance.appdriver.dnam.rmi.RmiSessionFactory;
import lombok.extern.slf4j.Slf4j;
import se.transmode.tnm.rmiclient.server.rmiserver.AbstractResponse;
import se.transmode.tnm.rmiclient.server.services.discovery.NodeEntry;
import se.transmode.tnm.rmiclient.server.services.discovery.NodesDiscoveryRequest;

import java.rmi.RemoteException;

import static com.infinera.metro.test.acceptance.appdriver.dnam.node.NodeApiUtil.getDefaultNodeEntry;

/**
 * Don't use this class directly, use appdriver.api.node.NodeApiFactory instead.
 * TODO: Move implementations to separate gradle modules? Or wait for Java 9 so we can hide implementation packages?
 */
@Slf4j
public class DnamNodeApi extends DnamRmiClient implements NodeApi {


    public DnamNodeApi(RemoteServiceAccessData remoteServiceAccessData) {
        super(new RmiSessionFactory(remoteServiceAccessData.getIpAddress(), remoteServiceAccessData.getPort()).getSession());
    }



    @Override
    public void addNode(Node node) {
        NodeEntry nodeEntry = getDefaultNodeEntry(node.getIpAddress());
        NodesDiscoveryRequest nodesDiscoveryRequest = NodesDiscoveryRequest.add(nodeEntry);
        process(nodesDiscoveryRequest);
    }

    public Node getNode(Node node) {
        AbstractResponse abstractResponse = process(NodesDiscoveryRequest.getNode(node.getIpAddress()));
        NodeEntry nodeEntry = abstractResponse.asNodesDiscoveryResponse().getNodeEntry();
        return new Node(nodeEntry.getUserRef());
    }

    public boolean assertNodeNotAdded(Node node) {
        try {
            getNode(node);
            return false;
        } catch (RuntimeException e) {
            return true;
        }
    }

    public void deleteNode(Node node) {
        NodeEntry nodeEntry = getDefaultNodeEntry(node.getIpAddress());
        process(NodesDiscoveryRequest.deleteNode(nodeEntry));

    }

    private AbstractResponse process(NodesDiscoveryRequest nodesDiscoveryRequest) {
        try {
            AbstractResponse abstractResponse = session.process(nodesDiscoveryRequest);
            checkResponse(abstractResponse);
            return abstractResponse;
        } catch (RemoteException e) {
            throw new RuntimeException("Failed to perform node request"
                .concat(nodesDiscoveryRequest.toString())
                .concat("Exception: ")
                .concat(e.getMessage()));
        }
    }

    private void checkResponse(AbstractResponse abstractResponse) {
        if(abstractResponse == null || abstractResponse.getReturnCode() != AbstractResponse.RESPONSE_OK) {
            throw new RuntimeException("Retrieved an erroneous response. Either response was null or response code was not RESPONSE_OK");
        }
    }
}

