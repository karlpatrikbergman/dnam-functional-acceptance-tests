package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApiFactory;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApiFactory.NodeApiType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("NodeAcceptanceTest")
@DisplayName("AddNodeAcceptanceTest")
class AddNodeAcceptanceTest {
    private static final String className = AddNodeAcceptanceTest.class.getSimpleName();

    @DisplayName("Add a node and verify the node was added")
    @Test
    void addANodeAndVerifyThatItWasAdded() {
        NodeApi nodeApi = NodeApiFactory.NODE_API_FACTORY.getNodeApi(NodeApiType.DNAM);
        nodeApi.addNode("172.17.3.4");
    }

}
