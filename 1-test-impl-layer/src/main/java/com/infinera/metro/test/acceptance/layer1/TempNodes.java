package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.test.acceptance.MetroManagementDslTest;
import org.junit.jupiter.api.Test;

public class TempNodes extends MetroManagementDslTest {

    @Test
    public void deleteNode1() {
        nodeApi.deleteNode(node1);
    }

    @Test
    public void deleteNode2() {
        nodeApi.deleteNode(node2);
    }
}
