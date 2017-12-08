package com.infinera.metro.test.acceptance.appdriver;

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
    void test() {
        log.info("Running some fast unit test for application driver");
    }
}
