package com.infinera.metro.test.acceptance.appdriver.dnam;

import lombok.Getter;
import se.transmode.tnm.rmiclient.server.rmiserver.Session;


public abstract class DnamRmiClient {
    @Getter protected final Session session;

    protected DnamRmiClient(Session session) {
        this.session = session;
    }
}
