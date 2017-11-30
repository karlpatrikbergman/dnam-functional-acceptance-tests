package com.infinera.metro.test.acceptance.appdriver.dnam.topology;

import se.transmode.tnm.rmiclient.server.services.connections.ports.AbstractPort;
import se.transmode.tnm.rmiclient.server.services.connections.topology.PeerComEntry;

import java.util.StringJoiner;

public class DnamTopologyUtil {


    public static PeerComEntry createPeerComEntry(String description, AbstractPort localPort, AbstractPort remotePort) {
        String theDescr = description;
        String theLocalIp = localPort.getAddress();
        int theLocalSubrack = localPort.getSubrack();
        int theLocalSlot = localPort.getSlot();
        int theLocalInterfaceNumber = localPort.getInterfaceNo();
        int theLocalPort = localPort.getPort();

        //These were set to 0 in actual request?
        //remoteSubrack=0,
        //remoteSlot=0,
        //remoteInterfaceNumber=0,
        //remotePort=0,

        String theRemoteIp = remotePort.getAddress();
        //int theRemoteSubrack = remotePort.getSubrack();
        //int theRemoteSlot = remotePort.getSlot();
        //int theRemoteInterfaceNumber = remotePort.getInterfaceNo();
        //int theRemotePort = remotePort.getPort();
        int theRemoteSubrack = 0; //Cant' remember why these values are zero?
        int theRemoteSlot = 0;
        int theRemoteInterfaceNumber = 0;
        int theRemotePort = 0;


        /**
         * From DNA-M user guide:
         * The Local Label setting controls naming of the Local Labels on peer connections in the DNA-M. The
         * two available choices (board name or node name), decides how the default name of these labels
         * are constructed.
         *
         * Local label A unique identifier for the interface in each node.
         */

        //localLabel=tp10g:1:2:2,
        //remoteLabel=tp10g:1:2:1,

        //String theLocalLabel = "tp10g:"+localPort.getSubrack()+":"+localPort.getSlot()+":"+localPort.getPort();
        //String theRemoteLabel = "tp10g:"+remotePort.getSubrack()+":"+remotePort.getSlot()+":"+remotePort.getPort();

        String theLocalLabel = createLabel(localPort.getBoardTypeText(), localPort.getSubrack(), localPort.getSlot(), localPort.getPort());
        String theRemoteLabel = createLabel(remotePort.getBoardTypeText(), remotePort.getSubrack(), remotePort.getSlot(), remotePort.getPort());

        int theLinkAttenuation = 0;             //Where get this?

        return new PeerComEntry(theDescr, theLocalIp, theLocalSubrack, theLocalSlot, theLocalInterfaceNumber, theLocalPort, theRemoteIp, theRemoteSubrack, theRemoteSlot, theRemoteInterfaceNumber, theRemotePort, theLocalLabel, theRemoteLabel, theLinkAttenuation);
    }

    private static String createLabel(String boardName, int subrack, int slot, int port) {
        StringJoiner sj = new StringJoiner(":");
        sj.add(boardName);
        sj.add(String.valueOf(subrack));
        sj.add(String.valueOf(slot));
        sj.add(String.valueOf(port));
        return sj.toString();
    }
}
