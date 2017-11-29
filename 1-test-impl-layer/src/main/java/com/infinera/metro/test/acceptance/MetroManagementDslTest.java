package com.infinera.metro.test.acceptance;

import com.infinera.metro.networkmanager.tools.docker.DockerUtil;
import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApiFactory;
import com.infinera.metro.test.acceptance.common.util.ExponentinalBackoff;
import lombok.extern.slf4j.Slf4j;

import static com.infinera.metro.test.acceptance.appdriver.api.node.NodeApiFactory.NODE_API_FACTORY;
import static com.infinera.metro.test.acceptance.common.DockerContainer.*;

@Slf4j
public abstract class MetroManagementDslTest {

    protected static final NodeApi nodeApi;
//    protected static final TopologyApi topologyApi;
    protected static final Node node1;
    protected static final Node node2;

    static {
        log.info("######## Retrieving NodeApi, TopologyApi and Nodes ({})");

        final String dnamServerIpAddress = getDockerContainerIpAddress(DNAM_SERVER.getName());
        nodeApi = NODE_API_FACTORY.getNodeApi(NodeApiFactory.NodeApiType.DNAM, new RemoteServiceAccessData(dnamServerIpAddress, 1099));

        node1 = new Node(getDockerContainerIpAddress(NODE_1.getName()));
        node2 = new Node(getDockerContainerIpAddress(NODE_2.getName()));

        //Add more api:s here, TopologyApi e.t.c.
    }

    private static String getDockerContainerIpAddress(String containerName) {
        return new ExponentinalBackoff().perform(() -> DockerUtil.DOCKER_UTIL.getContainerIpAddress(containerName));
    }
}
