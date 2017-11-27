package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.test.acceptance.MetroManagementDslTest;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.common.docker.DockerUtil;
import com.infinera.metro.test.acceptance.common.util.ExponentinalBackoff;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(Layer1TestFixture.class)
@DisplayName("NodeAcceptanceTest")
@Slf4j
class NodeAcceptanceTest extends MetroManagementDslTest {

    @DisplayName("Add node, get node and delete node")
    @Test
    void addNodeGetNotAndDeleteNode() {
//        log.info("felsökning");
//        final String dnamServerIpAddress = getDockerContainerIpAddress(DNAM_SERVER.getName());
//        NODE_API_FACTORY.getNodeApi(NodeApiFactory.NodeApiType.DNAM, new RemoteServiceAccessData(dnamServerIpAddress, 1099));
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

    private String getDockerContainerIpAddress(String containerName) {
        return new ExponentinalBackoff().perform(() -> DockerUtil.DOCKER_UTIL.getContainerIpAddress(containerName));
    }

}
