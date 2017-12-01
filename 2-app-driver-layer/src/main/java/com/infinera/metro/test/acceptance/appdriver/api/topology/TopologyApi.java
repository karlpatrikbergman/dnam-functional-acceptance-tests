package com.infinera.metro.test.acceptance.appdriver.api.topology;

import com.infinera.metro.test.acceptance.appdriver.dnam.topology.Peer;

public interface TopologyApi {
    void createPeerConnection(Port transmitPort, Port receivePort);
    Peer getPeer(Port port);
}
