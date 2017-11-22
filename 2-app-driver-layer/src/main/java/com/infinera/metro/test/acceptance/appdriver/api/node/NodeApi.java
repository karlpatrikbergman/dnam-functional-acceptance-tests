package com.infinera.metro.test.acceptance.appdriver.api.node;

public interface NodeApi {
    enum Type{DNAM}

    void addNode(String ipAddress);
}
