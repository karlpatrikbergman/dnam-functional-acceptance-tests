package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DnamServerExtension.class)
@Tag("DnamApplicationDriverTest")
@DisplayName("RmiConnectionTest")
@Slf4j
public class RmiConnectionTest {

    @DisplayName("Get session should return rmi session")
    @Test
    public void test() {
        log.info("Running rmi connection test");
    }

}
