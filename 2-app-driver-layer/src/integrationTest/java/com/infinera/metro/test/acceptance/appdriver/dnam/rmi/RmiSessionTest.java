package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

import com.infinera.metro.test.acceptance.appdriver.dnam.DnamServerExtension;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.DockerPort;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.transmode.tnm.rmiclient.server.rmiserver.Session;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * DnamServerExtension extends DockerComposeExtension which is a temporary workaround until DockerCompose works with
 * JUnit 5. It will bring up dnam-server et. al in docker containers before actual test.
 */
@ExtendWith(DnamServerExtension.class)
@Tag("DnamApplicationDriverTest")
@DisplayName("RmiSessionTest")
@Slf4j
class RmiSessionTest {

    @DisplayName("Get session should return rmi session")
    @Test
    void getRmiSessionTest(DockerComposeRule docker) throws DockerCertificateException, DockerException, InterruptedException {
        DockerPort dnamServerContainer = docker.containers().container("dnam-mainserver").port(1099);
        String url = dnamServerContainer.inFormat("$HOST:$EXTERNAL_PORT");
        log.info("DNA-M rmi server: {}", url);

        RmiSessionFactory rmiSessionFactory = new RmiSessionFactory(dnamServerContainer.getIp(), dnamServerContainer.getExternalPort());
        Session session = rmiSessionFactory.getSession();
        assertNotNull(session);
    }

}
