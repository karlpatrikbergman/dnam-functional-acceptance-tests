package com.infinera.metro.test.acceptance.common.docker;

import com.palantir.docker.compose.DockerComposeRule;
import org.junit.jupiter.api.extension.*;

/**
 * License/copyright:
 * https://github.com/c100k/docker-compose-rule-with-junit5/blob/master/LICENSE
 */

public class DockerComposeExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private final DockerComposeRule dockerComposeRule;

        public DockerComposeExtension(DockerComposeRule dockerComposeRule) {
            this.dockerComposeRule = dockerComposeRule;
    }
//    public DockerComposeExtension() {
//        dockerComposeRule = DockerComposeRule.builder()
//            .pullOnStartup(true)
//            .file("src/main/resources/docker-compose-dnam-server.yml")
//            .saveLogsTo("build/test-docker-logs")
//            .waitingForService("dnam-mainserver", toHaveAllPortsOpen())
//            .build();
//    }

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        dockerComposeRule.before();
    }

    public void afterAll(ExtensionContext extensionContext) throws Exception {
        dockerComposeRule.after();
    }

    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DockerComposeRule.class);
    }

    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return dockerComposeRule;
    }

}
