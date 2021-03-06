package com.infinera.metro.test.acceptance.appdriver.dnam.node;

import com.infinera.metro.networkmanager.tools.docker.DockerUtil;
import com.infinera.metro.networkmanager.tools.wait.Wait;
import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApiFactory;
import com.infinera.metro.test.acceptance.appdriver.dnam.DnamAppdriverTestFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(DnamAppdriverTestFixture.class)
@Tag("DnamApplicationDriverTest")
@DisplayName("DnamNodeApiTest")
@Slf4j
class DnamNodeApiTest {

    @DisplayName("Get DnamNodeApi should return instance of DnamNodeApi")
    @Test
    void test() {
        String ipAddress = new Wait().perform(() -> DockerUtil.DOCKER_UTIL.getContainerIpAddress("dnam-mainserver"));
        NodeApi nodeApi = NodeApiFactory.NODE_API_FACTORY.getNodeApi(NodeApiFactory.NodeApiType.DNAM, new RemoteServiceAccessData(ipAddress, 1099));
        assertNotNull(nodeApi);
    }
}
