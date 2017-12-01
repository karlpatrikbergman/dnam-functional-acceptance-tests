package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.dnam.acceptance.test.node.NodeAccessData;
import com.infinera.metro.dnam.acceptance.test.node.configuration.NodeConfiguration;
import com.infinera.metro.dnam.acceptance.test.node.configuration.serializedeserialize.ObjectFromFileUtilJackson;
import com.infinera.metro.networkmanager.tools.docker.DockerUtil;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.common.util.ExponentinalBackoff;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.infinera.metro.test.acceptance.common.DockerContainer.NODE_1;
import static com.infinera.metro.test.acceptance.common.DockerContainer.NODE_2;

class TempNodesConfig {
    private final NodeConfiguration nodeConfiguration;
    private final Map<String, NodeAccessData> nodeAccessDataMap;

    TempNodesConfig() {
        Node node1 = new Node(getDockerContainerIpAddress(NODE_1.getName()));
        Node node2 = new Node(getDockerContainerIpAddress(NODE_2.getName()));

        nodeAccessDataMap = new HashMap<>();
        nodeAccessDataMap.put("nodeA", NodeAccessData.createDefault(node1.getIpAddress())); //Note: use same names for nodes as in yaml-file
        nodeAccessDataMap.put("nodeZ", NodeAccessData.createDefault(node2.getIpAddress()));

        nodeConfiguration = ObjectFromFileUtilJackson
            .INSTANCE
            .getObject("layer1/layer1_configuration.yaml", NodeConfiguration.class)
            .copyWithNew(nodeAccessDataMap);
    }

    @Disabled
    @Test
    void applyConfiguration() {
        nodeConfiguration.apply(nodeAccessDataMap);
    }

    @Disabled
    @Test
    void deleteConfiguration() {
        nodeConfiguration.delete();
    }

    private String getDockerContainerIpAddress(String containerName) {
        return new ExponentinalBackoff().perform(() -> DockerUtil.DOCKER_UTIL.getContainerIpAddress(containerName));
    }
}
