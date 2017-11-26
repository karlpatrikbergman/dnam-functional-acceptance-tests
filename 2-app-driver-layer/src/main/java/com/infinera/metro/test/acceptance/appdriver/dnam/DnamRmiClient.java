package com.infinera.metro.test.acceptance.appdriver.dnam;

import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.dnam.rmi.RmiSessionFactory;
import lombok.Getter;
import se.transmode.tnm.rmiclient.server.rmiserver.Session;


public abstract class DnamRmiClient {
    @Getter protected final Session session;

    protected DnamRmiClient(RemoteServiceAccessData rmiRemoteService) {
        RmiSessionFactory rmiSessionFactory = new RmiSessionFactory(rmiRemoteService.getIpAddress(), rmiRemoteService.getPort());
        this.session = rmiSessionFactory.getSession();
    }
}
