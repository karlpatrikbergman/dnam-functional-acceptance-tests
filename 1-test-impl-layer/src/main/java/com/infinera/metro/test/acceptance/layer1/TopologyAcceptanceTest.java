package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.test.acceptance.MetroManagementDslTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//@ExtendWith(Layer1TestFixture.class)
@DisplayName("TopologyAcceptanceTest")
@Slf4j
class TopologyAcceptanceTest extends MetroManagementDslTest {



    @DisplayName("Create peer connection, get peer connection, delete peer connection")
    @Test
    void addGetAndDeletePeerConnection() {

//        nodeApi.addNode(node1);
//
//        String ipAddress = new ExponentinalBackoff().perform(() -> DockerUtil.DOCKER_UTIL.getContainerIpAddress("dnam-mainserver"));
//        DnamTopologyApi dnamTopologyApi = new DnamTopologyApi(new RemoteServiceAccessData(ipAddress, 1099));
//
//
//        Port fromPort = Port.builder()
//            .node(node1)
//            .subrack(1)
//            .slot(2)
//            .port(34)
//            .build();
//
//        Collection<NodeTopologyData> nodeTopologyData = dnamTopologyApi.getNodeTopologyWaitForPorts(Arrays.asList(node1));
//
//        nodeApi.deleteNode(node1);
    }

    @Ignore
    @Test
    public void addNode() {
//        nodeApi.addNode(node1);
    }

    @Ignore
    @Test
    public void deleteNode() {
//        nodeApi.deleteNode(node1);
    }

}
