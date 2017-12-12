package com.infinera.metro.test.acceptance.workbench;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@DisplayName("Workbench...")
@Slf4j
class WorkbenchTest {

    @Test
    void runningInDockerContainerTest() {
        Path path = Paths.get("/.dockerenv");

        if (Files.exists(path)) {
            log.info("I am running in a container!");
        } else {
            log.info("I am running on dev machine!");
        }
    }
}
