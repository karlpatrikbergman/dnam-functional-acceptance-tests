package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

import com.infinera.metro.test.acceptance.common.util.ExponentinalBackoff;
import lombok.extern.slf4j.Slf4j;
import se.transmode.tnm.rmiclient.server.rmiserver.Server;
import se.transmode.tnm.rmiclient.server.rmiserver.ServerDefs;
import se.transmode.tnm.rmiclient.server.rmiserver.ServerSessionType;
import se.transmode.tnm.rmiclient.server.rmiserver.Session;

@Slf4j
class RmiConnection {
    private final RmiServiceFactory rmiServiceFactory;
    private Session session;

    RmiConnection(RmiServiceFactory rmiServiceFactory) {
        this.rmiServiceFactory = rmiServiceFactory;
        createRmiSession();
    }

    Session getSession() {
        if (session == null) {
            createRmiSession();
        }
        return session;
    }

    private void createRmiSession() {
        log.info("Looking up remote Server stub...");
        final Server server = new ExponentinalBackoff().perform(() -> rmiServiceFactory.lookupRemoteService(Server.class, ServerDefs.SERVER_RMI_NAME));

        log.info("Creating rmi session...");
        this.session = new ExponentinalBackoff().perform(() -> server.createSession(ServerSessionType.WEBAPP));
    }
}
