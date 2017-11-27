package com.infinera.metro.test.acceptance.layer1;


import com.infinera.metro.dnam.acceptance.test.docker.DockerUtil2;
import com.infinera.metro.dnam.acceptance.test.node.NodeAccessData;
import com.infinera.metro.dnam.acceptance.test.node.configuration.NodeNetwork;
import com.infinera.metro.dnam.acceptance.test.node.configuration.serializedeserialize.ObjectFromFileUtilJackson;
import com.infinera.metro.test.acceptance.common.DockerContainer;
import com.palantir.docker.compose.DockerComposeRule;
import org.junit.jupiter.api.extension.*;

import java.util.HashMap;
import java.util.Map;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;

/**
 * This class sets up minimal test fixture for layer1 tests, but only once, and before any test is run.
 * When all tests are finished test fixture is teared down.
 * Since all tests use the same test fixture (dnam, dnam-db, nodes e.t.c ) it is very important that each test
 * cleans up after itself.
 * The reason for reusing the test fixture is the long startup time of dnam-mainserver.
 */
public class Layer1TestFixture implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private static DockerComposeRule docker;

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (docker == null) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> docker.after()));
            System.out.println("Setup test fixture");
            docker = DockerComposeRule.builder()
//                .pullOnStartup(true)
                .file("src/main/resources/layer1/docker-compose-layer1-test-fixture.yml")
                .saveLogsTo("build/layer1/test-docker-logs")
                .waitingForService("dnam-mainserver", toHaveAllPortsOpen())
                .build();
            docker.before();

            final String ipAddressNode1 = DockerUtil2.DOCKER_UTIL.getContainerIpAddress(docker, DockerContainer.NODE_1.getName());
            final String ipAddressNode2 = DockerUtil2.DOCKER_UTIL.getContainerIpAddress(docker, DockerContainer.NODE_2.getName());

            final NodeNetwork nodeNetwork = ObjectFromFileUtilJackson.INSTANCE.getObject("layer1/simple_layer1_example_network.yaml", NodeNetwork.class);
            NodeAccessData accessDataNodeA = NodeAccessData.createDefault(ipAddressNode1);
            NodeAccessData accessDataNodeZ = NodeAccessData.createDefault(ipAddressNode2);
            Map<String, NodeAccessData> nodeAccessDataMap = new HashMap<>();
            nodeAccessDataMap.put("nodeA", accessDataNodeA);
            nodeAccessDataMap.put("nodeZ", accessDataNodeZ);

            nodeNetwork.apply(nodeAccessDataMap);
        }
    }

    public void afterAll(ExtensionContext extensionContext) throws Exception {

    }

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DockerComposeRule.class);
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return docker;
    }

}