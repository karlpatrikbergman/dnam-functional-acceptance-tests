package com.infinera.metro.test.acceptance.layer1.killedboards;


import com.infinera.metro.dnam.acceptance.test.node.NodeAccessData;
import com.infinera.metro.dnam.acceptance.test.node.configuration.NodeConfiguration;
import com.infinera.metro.dnam.acceptance.test.node.configuration.serializedeserialize.ObjectFromFileUtilJackson;
import com.infinera.metro.test.acceptance.appdriver.api.topology.Port;
import com.palantir.docker.compose.DockerComposeRule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;

import java.util.HashMap;
import java.util.Map;

import static com.infinera.metro.test.acceptance.MetroManagementDslTest.node1;
import static com.infinera.metro.test.acceptance.MetroManagementDslTest.node2;

/**
 * This class sets up minimal test fixture for layer1 tests, but only once, and before any test is run.
 * When all tests are finished test fixture is teared down.
 * Since all tests use the same test fixture (dnam, dnam-db, nodes e.t.c ) it is very important that each test
 * cleans up after itself.
 * The reason for reusing the test fixture is the long startup time of dnam-mainserver.
 */
@Slf4j
class KilledBoardsTestDataConfigurer implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private static NodeConfiguration nodeConfiguration; //Don't leak to test implementation!
    @Getter private static Port nodeALinePort;
    @Getter private static Port nodeZLinePort;

    public void beforeAll(ExtensionContext extensionContext) {
        log.info("######## {} beforeAll()", KilledBoardsTestDataConfigurer.class.getSimpleName());

        /*
          To be done before each test class:
          - configure nodes using xtm-rest-client-api
          - create application driver objects based on node configuration, for use in test implementation
         */
        applyNodeConfiguration();
        createAppDriverDomainObjects();
    }

    private void applyNodeConfiguration() {
        log.info("######## Configuring nodes using xmt-rest-client");

        final Map<String, NodeAccessData> nodeAccessDataMap = new HashMap<>();
        nodeAccessDataMap.put("nodeA", NodeAccessData.createDefault(node1.getIpAddress())); //Note: use same names for nodes as in yaml-file
        nodeAccessDataMap.put("nodeZ", NodeAccessData.createDefault(node2.getIpAddress()));
        nodeConfiguration = ObjectFromFileUtilJackson.INSTANCE //Yaml-file contains node access data which we don't want
            .getObject("layer1/killedboards/killed_boards_network.yaml", NodeConfiguration.class)
            .copyWithNew(nodeAccessDataMap);
        nodeConfiguration.apply(nodeAccessDataMap);
    }

    private void createAppDriverDomainObjects() {
        log.info("######## Creating application driver domain objects");
    }

    public void afterAll(ExtensionContext extensionContext) {
        log.info("######## {} afterAll()", KilledBoardsTestDataConfigurer.class.getSimpleName());
        deleteNodeConfiguration();
    }

    private void deleteNodeConfiguration() {
        log.info("######## Deleting node configuration");
        nodeConfiguration.delete();
    }

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DockerComposeRule.class);
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return null;
    }

}