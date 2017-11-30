package com.infinera.metro.test.acceptance.appdriver.api.topology;

import com.infinera.metro.test.acceptance.appdriver.api.node.Node;

public interface TopologyApi {
    void createPeerConnection(Node transmitNode, Port transmitPort, Node receiveNode, Port receivePort);
}
