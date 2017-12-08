package com.infinera.metro.test.acceptance.layer1;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("fast")
@DisplayName("SomeFastUnitTest")
@Slf4j
class SomeFastUnitTest {

    @DisplayName("some fast unit test")
    @Test
    void someFastUnitTest() {
        log.info("Running some fast unit test for test implementation");
    }
}
