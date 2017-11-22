package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

import lombok.extern.slf4j.Slf4j;
import se.transmode.tnm.rmiclient.server.rmiserver.Server;
import se.transmode.tnm.rmiclient.server.rmiserver.ServerDefs;
import se.transmode.tnm.rmiclient.server.rmiserver.ServerSessionType;
import se.transmode.tnm.rmiclient.server.rmiserver.Session;

import java.rmi.RemoteException;

@Slf4j
public class RmiConnection {
    private final RmiServiceFactory rmiServiceFactory;
    private Session session;

    public RmiConnection(RmiServiceFactory rmiServiceFactory) {

        log.debug("Initializing {}", this.getClass().getSimpleName());

        this.rmiServiceFactory = rmiServiceFactory;
        createRmiSession();
    }

    public void createRmiSession() {

        log.debug("Creating rmi session");

        final Server server = rmiServiceFactory.lookupRemoteService(Server.class, ServerDefs.SERVER_RMI_NAME);
        while(session == null) {
            try {
                session = server.createSession(ServerSessionType.WEBAPP);
                if(session == null) {

                    log.debug("Failed to create rmi session {}, sleeping for 5 seconds");

                    Thread.sleep(5000);
                }
            } catch (RemoteException | InterruptedException e) {
                log.debug("Failed to create rmi session {}, sleeping for 5 seconds");
            }
        }
    }

    public Session getSession() {
        if(session == null) {
            createRmiSession();
        }
        return session;
    }
}
