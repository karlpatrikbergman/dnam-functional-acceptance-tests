package com.infinera.metro.test.acceptance.appdriver.dnam.topology;

import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.appdriver.api.topology.Port;
import lombok.Builder;

public class Peer extends Port {

    @Builder(builderMethodName = "peerBuilder")
    public Peer(Node node, String boardName, int subrack, int slot, int port) {
        super(node, boardName, subrack, slot, port);
    }
}
