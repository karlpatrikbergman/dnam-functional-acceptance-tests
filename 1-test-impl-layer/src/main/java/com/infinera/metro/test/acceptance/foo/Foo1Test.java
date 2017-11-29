package com.infinera.metro.test.acceptance.foo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("FooTests")
@DisplayName("FooAcceptanceTest1")
@Slf4j
public class Foo1Test extends AbstractFooTest {

    @Disabled
    @DisplayName("Running Foo1Test.test because I can")
    @Test
    public void test() {
        log.info("Running Foo1Test.test()");
    }
}
