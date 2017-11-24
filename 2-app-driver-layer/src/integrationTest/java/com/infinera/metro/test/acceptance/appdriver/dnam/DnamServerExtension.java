package com.infinera.metro.test.acceptance.appdriver.dnam;

import com.infinera.metro.test.acceptance.common.docker.DockerComposeExtension;
import com.palantir.docker.compose.DockerComposeRule;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;

/**
 * DockerComposeExtension is a temporary workaround until DockerCompose works with JUnit 5
 */
public class DnamServerExtension extends DockerComposeExtension {
    public DnamServerExtension() {
        super(DockerComposeRule.builder()
            .file("src/integrationTest/resources/docker-compose-dnam-server.yml")
            .saveLogsTo("build/test-docker-logs")
            .waitingForService("dnam-mainserver", toHaveAllPortsOpen())
            .build());
    }
}
