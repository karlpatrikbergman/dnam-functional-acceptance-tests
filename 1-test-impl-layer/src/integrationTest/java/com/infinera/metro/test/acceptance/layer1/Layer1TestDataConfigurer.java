package com.infinera.metro.test.acceptance.layer1;


import com.infinera.metro.dnam.acceptance.test.node.NodeAccessData;
import com.infinera.metro.dnam.acceptance.test.node.configuration.NodeConfiguration;
import com.infinera.metro.dnam.acceptance.test.node.configuration.Slot;
import com.infinera.metro.dnam.acceptance.test.node.configuration.Subrack;
import com.infinera.metro.dnam.acceptance.test.node.configuration.board.Mdu40EvenL;
import com.infinera.metro.dnam.acceptance.test.node.configuration.serializedeserialize.ObjectFromFileUtilJackson;
import com.infinera.metro.dnam.acceptance.test.node.mib.type.BoardType;
import com.infinera.metro.test.acceptance.appdriver.api.topology.Port;
import com.palantir.docker.compose.DockerComposeRule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.HashMap;
import java.util.Map;

import static com.infinera.metro.test.acceptance.MetroManagementDslTest.*;

/**
 * This class sets up minimal test fixture for layer1 tests, but only once, and before any test is run.
 * When all tests are finished test fixture is teared down.
 * Since all tests use the same test fixture (dnam, dnam-db, nodes e.t.c ) it is very important that each test
 * cleans up after itself.
 * The reason for reusing the test fixture is the long startup time of dnam-mainserver.
 */
@Slf4j
class Layer1TestDataConfigurer extends Layer1TestFixtureRunner {

    private static NodeConfiguration nodeConfiguration; //Don't leak to test implementation!
    @Getter private static Port nodeALinePort;
    @Getter private static Port nodeZLinePort;

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        super.beforeAll(extensionContext);

        log.info("######## {} beforeAll()", Layer1TestDataConfigurer.class.getSimpleName());

        /*
          To be done before each test class:
          - configure nodes using xtm-rest-client-api
          - create application driver objects based on node configuration, for use in test implementation
         */
        applyNodeConfiguration();
        createAppDriverDomainObjects();
    }

    private void applyNodeConfiguration() {

        log.info("######## Configuring nodes using xmt-rest-client and yaml-specification");

        final Map<String, NodeAccessData> nodeAccessDataMap = new HashMap<>();
        nodeAccessDataMap.put("nodeA", NodeAccessData.createDefault(node1.getIpAddress())); //Note: use same names for nodes as in yaml-file
        nodeAccessDataMap.put("nodeZ", NodeAccessData.createDefault(node2.getIpAddress()));
        nodeConfiguration = ObjectFromFileUtilJackson.INSTANCE //Yaml-file contains node access data which we don't want
            .getObject("layer1/layer1_configuration.yaml", NodeConfiguration.class)
            .copyWithNew(nodeAccessDataMap);
        nodeConfiguration.apply(nodeAccessDataMap);
    }

    private void createAppDriverDomainObjects() {

        log.info("######## Creating application driver domain objects based on configuration from yaml-file");

        final Mdu40EvenL transmitBoard = nodeConfiguration.getNodeEquipmentMap()
            .get("nodeA")
            .getBoard(Subrack.subrack1, Slot.slot3, Mdu40EvenL.class);

        nodeALinePort = Port.builder()
            .node(node1)
            .boardName(BoardType.MDU40EVENL.getValue())
            .subrack(transmitBoard.getSubrack().getValue())
            .slot(transmitBoard.getSlot().getValue())
            .port(transmitBoard.getLinePorts().get(0).getTransmitPort())
            .build();

        final Mdu40EvenL receiveBoard = nodeConfiguration.getNodeEquipmentMap()
            .get("nodeA")
            .getBoard(Subrack.subrack1, Slot.slot3, Mdu40EvenL.class);

        nodeZLinePort = Port.builder()
            .node(node2)
            .boardName(BoardType.MDU40EVENL.getValue())
            .subrack(receiveBoard.getSubrack().getValue())
            .slot(receiveBoard.getSlot().getValue())
            .port(receiveBoard.getLinePorts().get(0).getReceivePort())
            .build();
    }

    public void afterAll(ExtensionContext extensionContext) throws Exception {
        super.afterAll(extensionContext);

        log.info("######## {} afterAll()", Layer1TestDataConfigurer.class.getSimpleName());

        log.info("######## Deleting node configuration");
        nodeConfiguration.delete();


        log.info("######## Removing nodes from Network Manager (System under test");
        nodeApi.deleteNode(node1);
        nodeApi.deleteNode(node2);
    }

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DockerComposeRule.class);
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return null;
    }

}