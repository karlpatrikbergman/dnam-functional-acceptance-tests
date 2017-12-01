package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.dnam.acceptance.test.node.NodeAccessData;
import com.infinera.metro.dnam.acceptance.test.node.configuration.NodeConfiguration;
import com.infinera.metro.dnam.acceptance.test.node.configuration.Slot;
import com.infinera.metro.dnam.acceptance.test.node.configuration.Subrack;
import com.infinera.metro.dnam.acceptance.test.node.configuration.board.Mdu40EvenL;
import com.infinera.metro.dnam.acceptance.test.node.configuration.serializedeserialize.ObjectFromFileUtilJackson;
import com.infinera.metro.dnam.acceptance.test.node.mib.type.BoardType;
import com.infinera.metro.networkmanager.tools.docker.DockerUtil;
import com.infinera.metro.test.acceptance.MetroManagementDslTest;
import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.topology.Port;
import com.infinera.metro.test.acceptance.appdriver.dnam.topology.DnamTopologyApi;
import com.infinera.metro.test.acceptance.common.util.ExponentinalBackoff;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(Layer1TestFixture.class)
@DisplayName("TopologyAcceptanceTest")
@Slf4j
class TopologyAcceptanceTest extends MetroManagementDslTest {
    private static final String ipAddress = new ExponentinalBackoff().perform(() -> DockerUtil.DOCKER_UTIL.getContainerIpAddress("dnam-mainserver"));
    private static final DnamTopologyApi dnamTopologyApi = new DnamTopologyApi(new RemoteServiceAccessData(ipAddress, 1099));
    private static NodeConfiguration nodeConfiguration;

    @BeforeAll
    static void beforeAll() {
        Map<String, NodeAccessData> nodeAccessDataMap = new HashMap<>();
        nodeAccessDataMap.put("nodeA", NodeAccessData.createDefault(node1.getIpAddress())); //Note: use same names for nodes as in yaml-file
        nodeAccessDataMap.put("nodeZ", NodeAccessData.createDefault(node2.getIpAddress()));
        nodeConfiguration = ObjectFromFileUtilJackson.INSTANCE
            .getObject("layer1/layer1_configuration.yaml", NodeConfiguration.class)
            .copyWithNew(nodeAccessDataMap);
        nodeConfiguration.apply(nodeAccessDataMap);

        nodeApi.assertNodeNotAdded(node1);
        nodeApi.assertNodeNotAdded(node2);
    }

    @DisplayName("Create peer connection and verify it was created")
    @Test
    void createPeerConnectionAndVerifyItWasCreated() {
        log.info("######## Running test TopologyAcceptanceTest");

        nodeApi.addNode(node1);
        nodeApi.addNode(node2);

        Mdu40EvenL transmitBoard = nodeConfiguration.getNodeEquipmentMap()
            .get("nodeZ")
            .getBoard(Subrack.subrack1, Slot.slot3, Mdu40EvenL.class);

        Port transmitPort = Port.builder()
            .node(node1)
            .boardName(BoardType.MDU40EVENL.getValue())
            .subrack(transmitBoard.getSubrack().getValue())
            .slot(transmitBoard.getSlot().getValue())
            .port(transmitBoard.getLinePorts().get(0).getTransmitPort())
            .build();

        Mdu40EvenL receiveBoard = nodeConfiguration.getNodeEquipmentMap()
            .get("nodeA")
            .getBoard(Subrack.subrack1, Slot.slot3, Mdu40EvenL.class);

        Port receivePort = Port.builder()
            .node(node2)
            .boardName(BoardType.MDU40EVENL.getValue())
            .subrack(receiveBoard.getSubrack().getValue())
            .slot(receiveBoard.getSlot().getValue())
            .port(receiveBoard.getLinePorts().get(0).getReceivePort())
            .build();

        dnamTopologyApi.createPeerConnection(transmitPort, receivePort);

        Port peer = dnamTopologyApi.getPeers(transmitPort);
        assertNotNull(peer);
    }

    @AfterAll
    static void afterEach() {
        nodeApi.deleteNode(node1);
        nodeApi.deleteNode(node2);
        nodeConfiguration.delete();
    }
}