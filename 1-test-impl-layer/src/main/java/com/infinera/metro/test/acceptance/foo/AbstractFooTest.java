package com.infinera.metro.test.acceptance.foo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

@Slf4j
public class AbstractFooTest {
    @BeforeAll
    public static void setup() {
        log.info("Setup test fixture");
    }

    @AfterAll
    public static void tearDown() {
        log.info("Teardown test fixture");
    }

}
