package com.infinera.metro.test.acceptance.appdriver.api.topology;

public interface TopologyApi {
    void createPeerConnection(Port transmitPort, Port receivePort);
    Peer getPeer(Port port);
}
