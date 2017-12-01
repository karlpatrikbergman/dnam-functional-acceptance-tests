package com.infinera.metro.test.acceptance.appdriver.api.topology;


import com.infinera.metro.test.acceptance.appdriver.api.node.Node;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Port {
    private final Node node;
    private final String boardName;
    private final int subrack;
    private final int slot;
    private final int port;

    @Builder
    public Port(Node node, String boardName, int subrack, int slot, int port) {
        this.node = node;
        this.boardName = boardName;
        this.subrack = subrack;
        this.slot = slot;
        this.port = port;
    }

    public String getKey() {
        return node.getIpAddress() + "-" + subrack + ":" + slot + ":" + port;
    }

    public String getPeerKey() {
        return node.getIpAddress() + "-" + boardName + ":" + subrack + ":" + slot + ":" + port;
    }

}
