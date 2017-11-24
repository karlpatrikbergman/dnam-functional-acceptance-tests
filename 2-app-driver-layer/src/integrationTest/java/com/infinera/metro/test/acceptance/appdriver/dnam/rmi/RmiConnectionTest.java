package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.DockerPort;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.transmode.tnm.rmiclient.server.rmiserver.Session;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(DnamServerExtension.class)
@Tag("DnamApplicationDriverTest")
@DisplayName("RmiConnectionTest")
@Slf4j
public class RmiConnectionTest {

    @DisplayName("Get session should return rmi session")
    @Test
    public void test(DockerComposeRule docker) {
        DockerPort dnamServerContainer = docker.containers().container("dnam-mainserver").port(1099);
        String url = dnamServerContainer.inFormat("$HOST:$EXTERNAL_PORT");
        log.info("DNA-M rmi server: {}", url);

        RmiSessionFactory rmiSessionFactory = new RmiSessionFactory(dnamServerContainer.getIp(), dnamServerContainer.getExternalPort());
        Session session = rmiSessionFactory.getSession();
        assertNotNull(session);
    }

}
