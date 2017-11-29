package com.infinera.metro.test.acceptance.layer1;


import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;

/**
 * This class sets up minimal test fixture for layer1 tests, but only once, and before any test is run.
 * When all tests are finished test fixture is teared down.
 * Since all tests use the same test fixture (dnam, dnam-db, nodes e.t.c ) it is very important that each test
 * cleans up after itself.
 * The reason for reusing the test fixture is the long startup time of dnam-mainserver.
 */
@Slf4j
public class Layer1TestFixture implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private static DockerComposeRule docker;

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (docker == null) {

            log.info("######## DockerCompose rule to start docker containers for layer 1 tests");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> docker.after()));

            docker = DockerComposeRule.builder()
                .pullOnStartup(true)
                .file("src/main/resources/layer1/docker-compose-layer1-test-fixture.yml")
                .saveLogsTo("build/layer1/test-docker-logs")
                .waitingForService("dnam-mainserver", toHaveAllPortsOpen())
                .shutdownStrategy(ShutdownStrategy.GRACEFUL)
                .build();
            docker.before();
        }
    }

    public void afterAll(ExtensionContext extensionContext) throws Exception {}

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DockerComposeRule.class);
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return docker;
    }

}