package com.infinera.metro.test.acceptance.appdriver.dnam.node;

import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import com.infinera.metro.test.acceptance.appdriver.dnam.DnamRmiClient;
import lombok.extern.slf4j.Slf4j;
import se.transmode.tnm.rmiclient.server.services.discovery.NodeEntry;
import se.transmode.tnm.rmiclient.server.services.discovery.NodesDiscoveryRequest;
import se.transmode.tnm.rmiclient.server.services.discovery.NodesDiscoveryResponse;

import static com.infinera.metro.test.acceptance.appdriver.dnam.node.DnamNodeApiUtil.getDefaultNodeEntry;

/**
 * Don't use this class directly in tests, use appdriver.api.node.NodeApiFactory instead.
 * TODO: Move implementations to separate gradle modules? Or wait for Java 9 so we can hide implementation packages?
 */
@Slf4j
public class DnamNodeApi extends DnamRmiClient<NodesDiscoveryRequest, NodesDiscoveryResponse> implements NodeApi {

    public DnamNodeApi(RemoteServiceAccessData remoteServiceAccessData) {
        super(remoteServiceAccessData);
    }

    @Override
    public void addNode(Node node) {
        NodeEntry nodeEntry = getDefaultNodeEntry(node.getIpAddress());
        NodesDiscoveryRequest nodesDiscoveryRequest = NodesDiscoveryRequest.add(nodeEntry);
        process(nodesDiscoveryRequest);
    }

    public Node getNode(Node node) {
        NodesDiscoveryResponse nodesDiscoveryResponse = process(NodesDiscoveryRequest.getNode(node.getIpAddress()));
        NodeEntry nodeEntry = nodesDiscoveryResponse.getNodeEntry();
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
//
//    //TODO: Share code, generics
//    private NodesDiscoveryResponse process(NodesDiscoveryRequest nodesDiscoveryRequest) {
//        try {
//            NodesDiscoveryResponse nodesDiscoveryResponse = session.process(nodesDiscoveryRequest).asNodesDiscoveryResponse();
//            if(nodesDiscoveryResponse == null ||
//                nodesDiscoveryResponse.getReturnCode() != AbstractResponse.RESPONSE_OK) {
//                throw new RuntimeException("Retrieved an erroneous response for request: "
//                    .concat(nodesDiscoveryRequest.toString()
//                    .concat("\n")
//                    .concat("Response: ")
//                    .concat(nodesDiscoveryResponse.toString())));
//            }
//            return nodesDiscoveryResponse.asNodesDiscoveryResponse();
//        } catch (RemoteException e) {
//            throw new RuntimeException("Failed to perform node request"
//                .concat(nodesDiscoveryRequest.toString())
//                .concat("Exception: ")
//                .concat(e.getMessage()));
//        }
//    }
}

