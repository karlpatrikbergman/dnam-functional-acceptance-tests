package com.infinera.metro.test.acceptance.appdriver.dnam.node;

import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import com.infinera.metro.test.acceptance.appdriver.dnam.DnamRmiClient;
import com.infinera.metro.test.acceptance.appdriver.dnam.rmi.RmiSessionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Don't use this class directly, use appdriver.api.node.NodeApiFactory instead.
 * TODO: Move implementations to separate gradle modules? Or wait for Java 9 so we can hide implementation packages?
 */
@Slf4j
public class DnamNodeApi extends DnamRmiClient implements NodeApi {

    public DnamNodeApi(RemoteServiceAccessData remoteServiceAccessData) {
        super(new RmiSessionFactory(remoteServiceAccessData.getIpAddress(), remoteServiceAccessData.getPort()).getSession());
    }

    @Override
    public void addNode(String ipAddress) {
        log.info("Adding node with ip address {}to DNA-M", ipAddress);
    }
}

