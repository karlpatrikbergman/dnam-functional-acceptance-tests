package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.test.acceptance.MetroManagementDslTest;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("NodeAcceptanceTest")
@Slf4j
class NodeAcceptanceTest extends MetroManagementDslTest {

    @DisplayName("Add node, get node and delete node")
    @Test
    void addGetAndDeleteNode() {
        log.info("######## Running NodeAcceptanceTest");
        //given
        assertTrue(nodeApi.assertNodeNotAdded(node1));

        //when
        nodeApi.addNode(node1);

        //then
        Node node = nodeApi.getNode(node1);
        assertEquals(node1, node);

        //clean up
        nodeApi.deleteNode(node);
        assertTrue(nodeApi.assertNodeNotAdded(node));
        assertTrue(nodeApi.assertNodeNotAdded(node1));
    }
}