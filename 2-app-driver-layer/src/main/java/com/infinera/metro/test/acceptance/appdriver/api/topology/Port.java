package com.infinera.metro.test.acceptance.appdriver.api.topology;


import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Port {
    private final Node node;
    private final int subrack;
    private final int slot;
    private final int port;

    public String getKey() {
        return node.getIpAddress() + "-" + subrack + ":" + slot + ":" + port;
    }
}
