package com.infinera.metro.test.acceptance;

import com.infinera.metro.networkmanager.tools.docker.DockerUtil;
import com.infinera.metro.networkmanager.tools.wait.Wait;
import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApiFactory.NodeApiType;
import com.infinera.metro.test.acceptance.appdriver.api.topology.TopologyApi;
import com.infinera.metro.test.acceptance.appdriver.api.topology.TopologyApiFactory.TopologyApiType;
import lombok.extern.slf4j.Slf4j;

import static com.infinera.metro.test.acceptance.appdriver.api.node.NodeApiFactory.NODE_API_FACTORY;
import static com.infinera.metro.test.acceptance.appdriver.api.topology.TopologyApiFactory.TOPOLOGY_API_FACTORY;
import static com.infinera.metro.test.acceptance.common.DockerContainer.*;

@Slf4j
public abstract class MetroManagementDslTest {

    protected static final NodeApi nodeApi;
    protected static final TopologyApi topologyApi;
    protected static final Node node1;
    protected static final Node node2;

    static {

        log.info("######## Retrieving NodeApi, TopologyApi and Nodes");

        final String dnamServerIpAddress = getDockerContainerIpAddress(DNAM_SERVER.getName());
        final RemoteServiceAccessData remoteServiceAccessData = new RemoteServiceAccessData(dnamServerIpAddress, 1099);
        nodeApi = NODE_API_FACTORY.getNodeApi(NodeApiType.DNAM, remoteServiceAccessData);
        topologyApi = TOPOLOGY_API_FACTORY.getTopologyApi(TopologyApiType.DNAM, remoteServiceAccessData);

        node1 = new Node(getDockerContainerIpAddress(NODE_1.getName()));
        node2 = new Node(getDockerContainerIpAddress(NODE_2.getName()));

        //Add more api:s here...
    }

    private static String getDockerContainerIpAddress(String containerName) {
        return new Wait().perform(() -> DockerUtil.DOCKER_UTIL.getContainerIpAddress(containerName));
    }
}
