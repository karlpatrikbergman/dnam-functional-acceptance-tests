package com.infinera.metro.test.acceptance.appdriver.dnam.node;

import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApiFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Tag("ApplicationDriverTest")
@DisplayName("DnamApiTest")
public class DnamNodeApiTest {

    @DisplayName("Get NodeApi of type DNAM should return object not null")
    @Test
    public void getNodeApiOfTypeDnam() {
        NodeApi nodeApi = NodeApiFactory.NODE_API_FACTORY.getNodeApi(NodeApi.Type.DNAM);
        assertNotNull(nodeApi);

        nodeApi.addNode("172.2.4.4");
    }

}
