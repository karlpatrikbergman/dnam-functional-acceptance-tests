package com.infinera.metro.test.acceptance.appdriver.dnam.topology;

import com.github.rholder.retry.*;
import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.appdriver.api.topology.Port;
import com.infinera.metro.test.acceptance.appdriver.api.topology.TopologyApi;
import com.infinera.metro.test.acceptance.appdriver.dnam.DnamRmiClient;
import se.transmode.tnm.rmiclient.server.rmiserver.AbstractResponse;
import se.transmode.tnm.rmiclient.server.services.connections.ports.AbstractPort;
import se.transmode.tnm.rmiclient.server.services.connections.topology.NodeTopologyData;
import se.transmode.tnm.rmiclient.server.services.connections.topology.PeerComEntry;
import se.transmode.tnm.rmiclient.server.services.connections.topology.TopoRequest;
import se.transmode.tnm.rmiclient.server.services.connections.topology.TopoResponse;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static com.infinera.metro.test.acceptance.appdriver.dnam.topology.DnamTopologyUtil.createPeerComEntry;

public class DnamTopologyApi extends DnamRmiClient implements TopologyApi {

    public DnamTopologyApi(RemoteServiceAccessData remoteServiceAccessData) {
        super(remoteServiceAccessData);
    }

    public void createPeerConnection(Node transmitNode, Port transmitPort, Node receiveNode, Port receivePort) {
        final NodeTopologyData transmitNodeTopologydata = getNodePortsTopology(transmitNode.getIpAddress());
        final AbstractPort abstractPortTransmit = getAbstractPortTransmit(transmitPort, transmitNodeTopologydata);

        final NodeTopologyData receiveNodeTopologydata = getNodePortsTopology(receiveNode.getIpAddress());
        final AbstractPort abstractPortReceive = getAbstractPortReceive(receivePort, receiveNodeTopologydata);

        final PeerComEntry peerComEntryTransmitNode = createPeerComEntry("Peer node transmit ("+transmitNode.getIpAddress()+")", abstractPortTransmit, abstractPortReceive);
        final PeerComEntry peerComEntryReceiveNode = createPeerComEntry("Peer node receive ("+receiveNode.getIpAddress()+")", abstractPortReceive, abstractPortTransmit);

        transmitNodeTopologydata.setPeers(Collections.singletonList(peerComEntryTransmitNode));
        receiveNodeTopologydata.setPeers(Collections.singletonList(peerComEntryReceiveNode));

        setNodePortsTopology(transmitNodeTopologydata);
        setNodePortsTopology(receiveNodeTopologydata);
    }

    private AbstractPort getAbstractPortTransmit(Port port, NodeTopologyData nodeTopologyData) {
        return getAbstractPort(port, AbstractPort::isOutDirection, nodeTopologyData);
    }

    private AbstractPort getAbstractPortReceive(Port port, NodeTopologyData nodeTopologyData) {
        return getAbstractPort(port, AbstractPort::isInDirection, nodeTopologyData);
    }

    private AbstractPort getAbstractPort(Port port, Predicate<AbstractPort> direction, NodeTopologyData nodeTopologyData) {
        return nodeTopologyData.getPorts().stream()
            .filter(direction)
            .filter(port1 -> port1.getKey().equals(port.getKey()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Failed to find port in NodeTopologyData for node with ip "
                .concat(nodeTopologyData.getAddress())
                .concat(" matching test port ")
                .concat(port.getKey()))
            );
    }

    private void setNodePortsTopology(NodeTopologyData nodeTopologyData) {
        TopoRequest topoRequest = new TopoRequest(TopoRequest.TopoRequestType.REQ_TYPE_SET_NODE_DATA, Collections.singletonList(nodeTopologyData));
        TopoResponse topoResponse = process(topoRequest);
        assert topoResponse.getEntries() != null;
        assert topoRequest.getEntries().size() == 1;
        assert topoRequest.getEntries().iterator().next().getPeers().equals(nodeTopologyData.getPeers());
    }

    private NodeTopologyData getNodePortsTopology(String nodeIpNumber) {
        TopoRequest topoRequest = new TopoRequest(TopoRequest.TopoRequestType.REQ_TYPE_GET_NODE_DATA, nodeIpNumber, NodeTopologyData.PORTS);
        //noinspection ConstantConditions
        Retryer<Collection<NodeTopologyData>> retryer = RetryerBuilder.<Collection<NodeTopologyData>>newBuilder()
            .retryIfResult(Objects::isNull)
            .retryIfResult(Collection::isEmpty)
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
