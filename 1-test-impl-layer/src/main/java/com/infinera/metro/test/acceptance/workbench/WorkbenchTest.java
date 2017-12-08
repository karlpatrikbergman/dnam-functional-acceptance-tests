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

//    @Disabled
//    @Test
//    @DisplayName("Some workbench test")
//    void workbenchTest() throws InterruptedException, DockerException, DockerCertificateException {
//        log.info("Running some workbench test");
//        logContainerIp("node1");
//        logContainerIp("dnam-mainserver");
//    }
//
//    private void logContainerIp(String containerName) throws InterruptedException, DockerException, DockerCertificateException {
//        log.info("{} ip: {}", containerName, DockerUtil.DOCKER_UTIL.getContainerIpAddress(containerName));
//    }

//    @Test
//    void getIpAddressTest() throws UnknownHostException {
//        logContainerIp("node1");
//    }
//
//    private void logContainerIp(String containerName) throws UnknownHostException {
//        log.info("{} ip: {}", containerName, InetAddress.getByName("node1").getHostAddress());
//    }

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
