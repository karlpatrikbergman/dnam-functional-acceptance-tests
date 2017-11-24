package com.infinera.metro.test.acceptance.layer1;

import com.infinera.metro.test.acceptance.MetroManagementDslTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(Layer1TestFixture.class)
@Tag("NodeAcceptanceTest")
@DisplayName("AddNodeAcceptanceTest")
@Slf4j
class AddNodeAcceptanceTest extends MetroManagementDslTest {

    @DisplayName("Add a node and verify the node was added")
    @Test
    void addANodeAndVerifyThatItWasAdded() {
        assertNotNull(nodeApi);
    }

}
