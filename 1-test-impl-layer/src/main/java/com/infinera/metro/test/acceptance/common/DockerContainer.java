package com.infinera.metro.test.acceptance.common;

public enum DockerContainer {
    DNAM_SERVER("dnam-mainserver"),
    NODE_1("node1"),
    NODE_2("node2");

    private final String name;

    DockerContainer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
