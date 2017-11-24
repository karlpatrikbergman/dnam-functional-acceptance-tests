package com.infinera.metro.test.acceptance.appdriver.dnam.node;

import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import lombok.extern.slf4j.Slf4j;

/**
 * Don't use this class directly, use appdriver.api instead.
 * TODO: Move implementations to separate gradle modules? Or wait for Java 9 so we can hide implementation packages?
 */
@Slf4j
public class DnamNodeApi implements NodeApi {

    @Override
    public void addNode(String ipAddress) {
        log.info("Adding node with ip address {}to DNA-M", ipAddress);
    }
}

