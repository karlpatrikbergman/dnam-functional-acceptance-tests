package com.infinera.metro.test.acceptance.appdriver.dnam.node;

import com.infinera.metro.test.acceptance.appdriver.api.node.NodeApi;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Slf4j
public class DnamNodeApi implements NodeApi {

    @Override
    public void addNode(String ipAddress) {
        log.info("Adding node with ip address {}to DNA-M", ipAddress);
    }
}

