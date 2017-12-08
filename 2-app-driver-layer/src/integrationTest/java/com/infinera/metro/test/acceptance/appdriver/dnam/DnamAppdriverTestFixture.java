package com.infinera.metro.test.acceptance.appdriver.dnam;


import com.palantir.docker.compose.DockerComposeRule;
import org.junit.jupiter.api.extension.*;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;

public class DnamAppdriverTestFixture implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private static DockerComposeRule docker;

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (docker == null) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> docker.after()));
            System.out.println("Setup test fixture");
            docker = DockerComposeRule.builder()
//                .pullOnStartup(true)
                .file("src/integrationTest/resources/docker-compose-dnam-server.yml")
                .saveLogsTo("build/layer1/test-docker-logs")
                .waitingForService("dnam-mainserver", toHaveAllPortsOpen())
                .build();
            docker.before();
        }

    }

    public void afterAll(ExtensionContext extensionContext) {

    }

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DockerComposeRule.class);
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return docker;
    }

}