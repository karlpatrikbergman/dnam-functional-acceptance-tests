package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.networkmanager.tools.docker.DockerUtil;
import com.infinera.metro.test.acceptance.MetroManagementDslTest;
import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.topology.Port;
import com.infinera.metro.test.acceptance.appdriver.dnam.topology.DnamTopologyApi;
import com.infinera.metro.test.acceptance.common.util.ExponentinalBackoff;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//@ExtendWith(Layer1TestFixture.class)
@DisplayName("TopologyAcceptanceTest")
@Slf4j
class TopologyAcceptanceTest extends MetroManagementDslTest {

    private static String ipAddress = new ExponentinalBackoff().perform(() -> DockerUtil.DOCKER_UTIL.getContainerIpAddress("dnam-mainserver"));
    private static DnamTopologyApi dnamTopologyApi = new DnamTopologyApi(new RemoteServiceAccessData(ipAddress, 1099));
//    private static NodeNetwork nodeNetwork;

//    @BeforeAll
//    public static void beforeAll(DockerComposeRule dockerComposeRule) throws IOException {
//        nodeNetwork = ObjectFromFileUtilJackson.INSTANCE.getObject("layer1/simple_layer1_example_network.yaml", NodeNetwork.class);
//        Map<String, NodeAccessData> nodeAccessDataMap = new HashMap<>();
//        nodeAccessDataMap.put("nodeA", NodeAccessData.createDefault(node1.getIpAddress())); //Note: use same names for nodes as in yaml-file
//        nodeAccessDataMap.put("nodeZ", NodeAccessData.createDefault(node2.getIpAddress()));
//        nodeNetwork.apply(nodeAccessDataMap);
//    }

    @BeforeEach
    public void beforeEach() {
        nodeApi.assertNodeNotAdded(node1);
        nodeApi.assertNodeNotAdded(node2);
    }

    @DisplayName("Create peer connection, get peer connection, delete peer connection")
    @Test
    void addGetAndDeletePeerConnection() {
        log.info("######## Running test TopologyAcceptanceTest");

        nodeApi.addNode(node1);
        nodeApi.addNode(node2);

        Port transmitPort = Port.builder()
            .node(node1)
            .subrack(1)
            .slot(3)
            .port(81)
            .build();

        Port receivePort = Port.builder()
            .node(node2)
            .subrack(1)
            .slot(3)
            .port(82)
            .build();

        dnamTopologyApi.createPeerConnection(node1, transmitPort, node2, receivePort);

        //TODO: Get peer config and verify it

    }

    @AfterEach
    public void afterEach() {
        nodeApi.deleteNode(node1);
        nodeApi.deleteNode(node2);
        //TODO: Clean node configuration!!
    }
}
