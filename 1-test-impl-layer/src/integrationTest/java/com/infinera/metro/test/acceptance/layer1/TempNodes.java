package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.test.acceptance.MetroManagementDslTest;
import org.junit.jupiter.api.Test;

class TempNodes extends MetroManagementDslTest {

    @Test
    void deleteNode1() {
        nodeApi.deleteNode(node1);
    }

    @Test
    void deleteNode2() {
        nodeApi.deleteNode(node2);
    }
}