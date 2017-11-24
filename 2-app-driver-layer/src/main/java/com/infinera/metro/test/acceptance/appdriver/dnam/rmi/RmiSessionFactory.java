package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

import lombok.Value;
import se.transmode.tnm.rmiclient.server.rmiserver.Session;


/**
 * Should this be Singleton or not?
 * Should we have a session pool?
 * How about thread safety, can instances of Session be shared between threads?
 */
@Value
public class RmiSessionFactory {

    private final String ipAddress;
    private final int port;
    private RmiConnection rmiConnection;

    public RmiSessionFactory(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.rmiConnection = new RmiConnection(new RmiServiceFactory(ipAddress, port));
    }

    public Session getSession() {
        return rmiConnection.getSession();
    }
}
