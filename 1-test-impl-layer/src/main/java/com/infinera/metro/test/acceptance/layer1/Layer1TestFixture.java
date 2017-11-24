package com.infinera.metro.test.acceptance.layer1;


import com.palantir.docker.compose.DockerComposeRule;
import org.junit.jupiter.api.extension.*;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;

public class Layer1TestFixture implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private static DockerComposeRule docker;

    public Layer1TestFixture() {
        if(docker == null) {
            docker = DockerComposeRule.builder()
                .pullOnStartup(true)
                .file("src/main/resources/layer1/docker-compose-layer1-test-fixture.yml")
                .saveLogsTo("build/layer1/test-docker-logs")
                .waitingForService("dnam-mainserver", toHaveAllPortsOpen())
                .build();
        }
    }

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        docker.before();
    }

    public void afterAll(ExtensionContext extensionContext) throws Exception {
        docker.after();
    }

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DockerComposeRule.class);
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return docker;
    }

}