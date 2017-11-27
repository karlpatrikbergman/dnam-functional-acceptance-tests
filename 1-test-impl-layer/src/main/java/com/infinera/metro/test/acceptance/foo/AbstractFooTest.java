package com.infinera.metro.test.acceptance.foo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

@Slf4j
public class AbstractFooTest {

    private static boolean testFixtureIsUp = false;

    @BeforeAll
    public static void setup() {
        if(testFixtureIsUp == false) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Tear down foo test fixture")));
            System.out.println("Setup test fixture");
            testFixtureIsUp = true;
        }
    }
}
