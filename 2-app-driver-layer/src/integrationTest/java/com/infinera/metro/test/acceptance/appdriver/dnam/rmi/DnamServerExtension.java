package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

import com.infinera.metro.test.acceptance.common.docker.DockerComposeExtension;
import com.palantir.docker.compose.DockerComposeRule;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;

public class DnamServerExtension extends DockerComposeExtension {
    public DnamServerExtension() {
        super(DockerComposeRule.builder()
//            .pullOnStartup(true)
            .file("src/integrationTest/resources/docker-compose-dnam-server.yml")
            .saveLogsTo("build/test-docker-logs")
            .waitingForService("dnam-mainserver", toHaveAllPortsOpen())
//            .shutdownStrategy(ShutdownStrategy.SKIP)
            .build());
    }

}
