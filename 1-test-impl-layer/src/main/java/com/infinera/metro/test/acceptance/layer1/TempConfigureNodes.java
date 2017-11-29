package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.dnam.acceptance.test.node.NodeAccessData;
import com.infinera.metro.dnam.acceptance.test.node.configuration.NodeNetwork;
import com.infinera.metro.dnam.acceptance.test.node.configuration.serializedeserialize.ObjectFromFileUtilJackson;
import com.infinera.metro.networkmanager.tools.docker.DockerUtil;
import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import com.infinera.metro.test.acceptance.common.util.ExponentinalBackoff;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.infinera.metro.test.acceptance.common.DockerContainer.NODE_1;
import static com.infinera.metro.test.acceptance.common.DockerContainer.NODE_2;

public class TempConfigureNodes {

    @Test
    public void configureNodes() {


        Node node1 = new Node(getDockerContainerIpAddress(NODE_1.getName()));
        Node node2 = new Node(getDockerContainerIpAddress(NODE_2.getName()));

        NodeNetwork nodeNetwork = ObjectFromFileUtilJackson.INSTANCE.getObject("layer1/simple_layer1_example_network.yaml", NodeNetwork.class);
        Map<String, NodeAccessData> nodeAccessDataMap = new HashMap<>();
        nodeAccessDataMap.put("nodeA", NodeAccessData.createDefault(node1.getIpAddress())); //Note: use same names for nodes as in yaml-file
        nodeAccessDataMap.put("nodeZ", NodeAccessData.createDefault(node2.getIpAddress()));
        nodeNetwork.apply(nodeAccessDataMap);

    }

    private String getDockerContainerIpAddress(String containerName) {
        return new ExponentinalBackoff().perform(() -> DockerUtil.DOCKER_UTIL.getContainerIpAddress(containerName));
    }
}
