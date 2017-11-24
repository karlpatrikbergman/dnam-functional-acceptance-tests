package com.infinera.metro.test.acceptance.appdriver.api;

import lombok.Value;

@Value
public class RemoteServiceAccessData {
    private final String ipAddress;
    private final int port;
}
