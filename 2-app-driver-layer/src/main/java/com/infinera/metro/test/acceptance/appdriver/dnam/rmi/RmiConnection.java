package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

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

        //        Server server = lookupServerStub();
//        int numberOfAttempts = 0;
//        while (session == null) {
//            try {
//                session = server.createSession(ServerSessionType.WEBAPP);
//            } catch (RemoteLookupFailureException | RemoteException e) {
//                log.error("{}\n Sleeping...", e.getMessage());
//                sleep(10);
//            }
//            if(++numberOfAttempts >= 10) {
//                throw new RuntimeException("Failed to create Session. Stopped after 10 attempts, sleeping 10 secs " +
//                    "between each attempt");
//            }
//        }
    }

//    private Server lookupServerStub() {
//        log.info("Lookingup remote server stub...");
//        Server server = null;
//        int numberOfAttempts = 0;
//        while(server == null) {
//            try {
//                server = rmiServiceFactory.lookupRemoteService(Server.class, ServerDefs.SERVER_RMI_NAME);
//            } catch (RuntimeException e) {
//                log.error("{}\n Sleeping...", e.getMessage());
//                sleep(5);
//            }
//            if(++numberOfAttempts >= 5) {
//                throw new RuntimeException("Failed to lookup remi ServerStub. Stopped after 5 attempts, sleeping 5 secs " +
//                    "between each attempt");
//            }
//        }
//        return server;
//    }
}
