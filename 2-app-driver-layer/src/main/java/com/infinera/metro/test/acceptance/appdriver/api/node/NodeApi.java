package com.infinera.metro.test.acceptance.appdriver.api.node;

public interface NodeApi {
    void addNode(Node node);
    Node getNode(Node node);
    void deleteNode(Node node);
    boolean assertNodeNotAdded(Node node);
}
