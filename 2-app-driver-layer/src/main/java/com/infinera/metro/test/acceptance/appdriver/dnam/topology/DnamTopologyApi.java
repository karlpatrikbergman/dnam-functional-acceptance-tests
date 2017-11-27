package com.infinera.metro.test.acceptance.appdriver.dnam.topology;

import com.github.rholder.retry.*;
import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.appdriver.dnam.DnamRmiClient;
import se.transmode.tnm.rmiclient.server.rmiserver.AbstractResponse;
import se.transmode.tnm.rmiclient.server.services.connections.topology.NodeTopologyData;
import se.transmode.tnm.rmiclient.server.services.connections.topology.TopoRequest;
import se.transmode.tnm.rmiclient.server.services.connections.topology.TopoResponse;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DnamTopologyApi extends DnamRmiClient {

    public DnamTopologyApi(RemoteServiceAccessData remoteServiceAccessData) {
        super(remoteServiceAccessData);
    }



    public Collection<NodeTopologyData> getNodeTopologyWaitForPorts(Collection<Node> nodes) {
        try {
            return getNodeTopologyWaitForPortsImpl(nodes.stream()
                .map(Node::getIpAddress)
                .collect(Collectors.toList()));
        } catch (RemoteException | InterruptedException e) {
            throw new RuntimeException("Failed to getNodeTopology for nodes: "
                .concat(nodes.toString())
                .concat("Exception: ")
                .concat(e.getMessage()));
        }
    }

    Collection<NodeTopologyData> getNodeTopologyWaitForPortsImpl(Collection<String> nodeIpNumbers) throws RemoteException, InterruptedException {
        TopoRequest topoRequest = new TopoRequest(TopoRequest.TopoRequestType.REQ_TYPE_GET_NODE_DATA, nodeIpNumbers, NodeTopologyData.PORTS);
        Retryer<Collection<NodeTopologyData>> retryer = RetryerBuilder.<Collection<NodeTopologyData>>newBuilder()
            .retryIfResult(Objects::isNull)
            .retryIfException()
            .retryIfRuntimeException()
            .withWaitStrategy(WaitStrategies.exponentialWait(1000, 5, TimeUnit.SECONDS))
            .withStopStrategy(StopStrategies.stopAfterAttempt(300))
            .build();
        try {
            return retryer.call(() -> process(topoRequest).getEntries());
        } catch (RetryException | ExecutionException e) {
            throw new RuntimeException("Failed to get ports for nodes with ip numbers "
                .concat(nodeIpNumbers.toString())
                .concat(e.getMessage()));
        }
    }

    //TODO: Share code
    private TopoResponse process(TopoRequest topoRequest) {
        try {
            AbstractResponse abstractResponse = session.process(topoRequest);
            checkResponse(abstractResponse);
            return (TopoResponse) abstractResponse;
        } catch (RemoteException e) {
            throw new RuntimeException("Failed to perform node request"
                .concat(topoRequest.toString())
                .concat("Exception: ")
                .concat(e.getMessage()));
        }
    }

    //TODO: Share code
    private void checkResponse(AbstractResponse abstractResponse) {
        if(abstractResponse == null ||
                abstractResponse.getReturnCode() != AbstractResponse.RESPONSE_OK) {
            throw new RuntimeException("Retrieved an erroneous response. Either response was null or return code was not RESPONSE_OK");
        }
    }
}
