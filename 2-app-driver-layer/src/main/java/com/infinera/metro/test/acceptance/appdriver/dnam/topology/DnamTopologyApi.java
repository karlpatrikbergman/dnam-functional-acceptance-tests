package com.infinera.metro.test.acceptance.appdriver.dnam.topology;

import com.github.rholder.retry.*;
import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.appdriver.api.topology.Port;
import com.infinera.metro.test.acceptance.appdriver.dnam.DnamRmiClient;
import se.transmode.tnm.rmiclient.server.rmiserver.AbstractResponse;
import se.transmode.tnm.rmiclient.server.services.connections.ports.AbstractPort;
import se.transmode.tnm.rmiclient.server.services.connections.topology.NodeTopologyData;
import se.transmode.tnm.rmiclient.server.services.connections.topology.TopoRequest;
import se.transmode.tnm.rmiclient.server.services.connections.topology.TopoResponse;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class DnamTopologyApi extends DnamRmiClient {

    public DnamTopologyApi(RemoteServiceAccessData remoteServiceAccessData) {
        super(remoteServiceAccessData);
    }

    public void nodeHasTransmitPort(Node node, Port port) {
        nodeHasReceivePort(node, port, abstractPort -> abstractPort.isOutDirection());
    }

    public void nodeHasReceivePort(Node node, Port port) {
        nodeHasReceivePort(node, port, abstractPort -> abstractPort.isInDirection());
    }

    void nodeHasReceivePort(Node node, Port port, Predicate<AbstractPort> direction) {
        NodeTopologyData nodePortsTopology = getNodePortsTopology(node.getIpAddress());
        nodePortsTopology.getPorts().stream()
            .filter(direction)
            .filter(port1 -> port1.getKey().equals(port.getKey()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Failed to find port in NodeTopologyData for node with ip "
                .concat(node.getIpAddress())
                .concat(" matching test port ")
                .concat(port.getKey()))
            );
    }

    NodeTopologyData getNodePortsTopology(String nodeIpNumber) {
        TopoRequest topoRequest = new TopoRequest(TopoRequest.TopoRequestType.REQ_TYPE_GET_NODE_DATA, nodeIpNumber, NodeTopologyData.PORTS);
        TopoResponse topoResponse = process(topoRequest);
        Retryer<Collection<NodeTopologyData>> retryer = RetryerBuilder.<Collection<NodeTopologyData>>newBuilder()
            .retryIfResult(Objects::isNull)
            .retryIfResult(nodeTopologyCollection -> nodeTopologyCollection.isEmpty())
            .retryIfResult(nodeTopologyCollection -> nodeTopologyCollection.iterator().next().getPorts().isEmpty())
            .retryIfException()
            .retryIfRuntimeException()
            .withWaitStrategy(WaitStrategies.exponentialWait(1000, 5, TimeUnit.SECONDS))
            .withStopStrategy(StopStrategies.stopAfterAttempt(300))
            .build();
        try {
            Collection<NodeTopologyData> nodeTopologyDataCollection = retryer.call(() -> process(topoRequest).getEntries());
            assert nodeTopologyDataCollection.size() == 1;
            return nodeTopologyDataCollection.iterator().next();
        } catch (RetryException | ExecutionException e) {
            throw new RuntimeException("Failed to get ports for nodes with ip numbers "
                .concat(nodeIpNumber)
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
