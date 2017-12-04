package com.infinera.metro.test.acceptance.appdriver.dnam.topology;

import com.github.rholder.retry.*;
import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.topology.Peer;
import com.infinera.metro.test.acceptance.appdriver.api.topology.Port;
import com.infinera.metro.test.acceptance.appdriver.api.topology.TopologyApi;
import com.infinera.metro.test.acceptance.appdriver.dnam.DnamRmiClient;
import se.transmode.tnm.rmiclient.server.services.connections.ports.AbstractPort;
import se.transmode.tnm.rmiclient.server.services.connections.topology.NodeTopologyData;
import se.transmode.tnm.rmiclient.server.services.connections.topology.PeerComEntry;
import se.transmode.tnm.rmiclient.server.services.connections.topology.TopoRequest;
import se.transmode.tnm.rmiclient.server.services.connections.topology.TopoResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static com.infinera.metro.test.acceptance.appdriver.dnam.topology.DnamTopologyUtil.createPeerComEntry;

public class DnamTopologyApi extends DnamRmiClient<TopoRequest, TopoResponse> implements TopologyApi {

    public DnamTopologyApi(RemoteServiceAccessData remoteServiceAccessData) {
        super(remoteServiceAccessData);
    }

    public void createPeerConnection(Port transmitPort, Port receivePort) {

        final com.google.common.base.Predicate<Collection<NodeTopologyData>> portsCollectionIsEmpty =
            nodeTopologyDataCollection -> nodeTopologyDataCollection.iterator().next().getPorts().isEmpty();

        final NodeTopologyData transmitNodeTopologydata = getNodeTopology(transmitPort.getNode().getIpAddress(), portsCollectionIsEmpty);
        final AbstractPort abstractPortTransmit = getAbstractPortTransmit(transmitPort, transmitNodeTopologydata);

        final NodeTopologyData receiveNodeTopologydata = getNodeTopology(receivePort.getNode().getIpAddress(), portsCollectionIsEmpty);
        final AbstractPort abstractPortReceive = getAbstractPortReceive(receivePort, receiveNodeTopologydata);

        final PeerComEntry peerComEntryTransmitNode = createPeerComEntry("Peer node transmit ("+transmitPort.getNode().getIpAddress()+")",
            abstractPortTransmit, abstractPortReceive);

        final PeerComEntry peerComEntryReceiveNode = createPeerComEntry("Peer node receive ("+receivePort.getNode().getIpAddress()+")",
            abstractPortReceive, abstractPortTransmit);

        transmitNodeTopologydata.setPeers(Collections.singletonList(peerComEntryTransmitNode));
        receiveNodeTopologydata.setPeers(Collections.singletonList(peerComEntryReceiveNode));

        setNodePortsTopology(transmitNodeTopologydata);
        setNodePortsTopology(receiveNodeTopologydata);
    }

    public Peer getPeer(Port port) {
        final com.google.common.base.Predicate<Collection<NodeTopologyData>> peersCollectionIsEmpty =
            nodeTopologyDataCollection -> nodeTopologyDataCollection.iterator().next().getPeers().isEmpty();

        final NodeTopologyData nodeTopologyData = getNodeTopology(port.getNode().getIpAddress(), peersCollectionIsEmpty);

        final PeerComEntry peerComEntry = nodeTopologyData.getPeers().stream()
            .filter(entry -> entry.getLocalKey().equalsIgnoreCase(port.getPeerKey()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Failed to find a PeerComEntry for node "
                .concat(port.getNode().toString())
                .concat(" matching port "
                .concat(port.toString()))));

        final AbstractPort abstractPort = nodeTopologyData.getPorts().stream()
            .filter(port1 -> port1.getKey().equals(port.getKey()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Failed to find a AbstractPort for node "
                .concat(port.getNode().toString())
                .concat(" matching port "
                    .concat(port.toString()))));

        return Peer.peerBuilder()
            .node(port.getNode())
            .boardName((abstractPort.getBoardTypeText()))
            .subrack(peerComEntry.getLocalSubrack())
            .slot(peerComEntry.getLocalSlot())
            .port(peerComEntry.getLocalPort())
            .build();
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
                .concat(" matching test port with key ")
                .concat(port.getPeerKey()))
            );
    }

    private void setNodePortsTopology(NodeTopologyData nodeTopologyData) {
        TopoRequest topoRequest = new TopoRequest(TopoRequest.TopoRequestType.REQ_TYPE_SET_NODE_DATA, Collections.singletonList(nodeTopologyData));
        TopoResponse topoResponse = process(topoRequest);
        assert topoResponse.getEntries() != null;
        assert topoRequest.getEntries().size() == 1;
        assert topoRequest.getEntries().iterator().next().getPeers().equals(nodeTopologyData.getPeers());
    }

    //TODO: Will it be significantly faster to get only ports when only ports are needed?
    private NodeTopologyData getNodeTopology(String nodeIpNumber, com.google.common.base.Predicate<Collection<NodeTopologyData>> specificDataNotEmtpy) {
        TopoRequest topoRequest = new TopoRequest(TopoRequest.TopoRequestType.REQ_TYPE_GET_NODE_DATA, nodeIpNumber, NodeTopologyData.ALL_DATA);
        //noinspection ConstantConditions
        Retryer<Collection<NodeTopologyData>> retryer = RetryerBuilder.<Collection<NodeTopologyData>>newBuilder()
            .retryIfResult(Objects::isNull)
            .retryIfResult(Collection::isEmpty)
            .retryIfResult(specificDataNotEmtpy)
            .retryIfException()
            .retryIfRuntimeException()
            .withWaitStrategy(WaitStrategies.exponentialWait(1000, 2, TimeUnit.SECONDS))
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
//
//    //TODO: Share code
//    private TopoResponse process(TopoRequest topoRequest) {
//        try {
//            AbstractResponse abstractResponse = session.process(topoRequest);
//            checkResponse(abstractResponse);
//            return (TopoResponse) abstractResponse;
//        } catch (RemoteException e) {
//            throw new RuntimeException("Failed to perform node request"
//                .concat(topoRequest.toString())
//                .concat("Exception: ")
//                .concat(e.getMessage()));
//        }
//    }
//
//    //TODO: Share code
//    private void checkResponse(AbstractResponse abstractResponse) {
//        if(abstractResponse == null ||
//                abstractResponse.getReturnCode() != AbstractResponse.RESPONSE_OK) {
//            throw new RuntimeException("Retrieved an erroneous response. Either response was null or return code was not RESPONSE_OK");
//        }
//    }
}
