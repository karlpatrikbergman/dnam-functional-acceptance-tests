package com.infinera.metro.test.acceptance;

import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApiFactory;
import com.infinera.metro.test.acceptance.common.docker.DockerUtil;
import com.infinera.metro.test.acceptance.common.util.ExponentinalBackoff;

public abstract class MetroManagementDslTest {

    protected final NodeApi nodeApi;

    public MetroManagementDslTest() {
        String ipAddress = new ExponentinalBackoff().perform(() -> DockerUtil.DOCKER_UTIL.getContainerIpAddress("dnam-mainserver"));
        this.nodeApi = NodeApiFactory.NODE_API_FACTORY.getNodeApi(NodeApiFactory.NodeApiType.DNAM, new RemoteServiceAccessData(ipAddress, 1099));
        //Add more api:s here, TopologyApi e.t.c.
    }

}
