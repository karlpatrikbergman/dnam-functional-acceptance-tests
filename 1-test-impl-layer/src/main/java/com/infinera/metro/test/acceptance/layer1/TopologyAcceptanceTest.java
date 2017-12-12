package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.test.acceptance.MetroManagementDslTest;
import com.infinera.metro.test.acceptance.appdriver.api.topology.Port;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(Layer1TestDataConfigurer.class)
@DisplayName("TopologyAcceptanceTest")
@Slf4j
class TopologyAcceptanceTest extends MetroManagementDslTest {

    @DisplayName("Create peer connection and verify it was created")
    @Test
    void createPeerConnectionAndVerifyItWasCreated() {

        log.info("######## Running test TopologyAcceptanceTest");

        nodeApi.assertNodeNotAdded(node1);
        nodeApi.assertNodeNotAdded(node2);


        nodeApi.addNode(node1);
        nodeApi.addNode(node2);

        final Port nodeALinePort = Layer1TestDataConfigurer.getNodeALinePort();
        final Port nodeZLinePort = Layer1TestDataConfigurer.getNodeZLinePort();

        //Would it be better with something like:
        //node.configureWithMdu40EvenLBoard() ??

        topologyApi.createPeerConnection(nodeALinePort, nodeZLinePort);

        Port peer = topologyApi.getPeer(nodeALinePort);

        assertNotNull(peer);

//        nodeApi.deleteNode(node1);
//        nodeApi.deleteNode(node2);
    }
}