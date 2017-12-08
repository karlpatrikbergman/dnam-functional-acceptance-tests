package com.infinera.metro.test.acceptance.appdriver.dnam.node;

import se.transmode.tnm.crypto.PasswordEncrypter;
import se.transmode.tnm.model.nodes.AlarmHandling;
import se.transmode.tnm.model.nodes.NodeFamily;
import se.transmode.tnm.model.nodes.NodeRef;
import se.transmode.tnm.model.nodes.TopologyUsed;
import se.transmode.tnm.model.nodes.details.EnmLoginDetails;
import se.transmode.tnm.model.nodes.details.FtpLoginDetails;
import se.transmode.tnm.model.nodes.inventory.SubrackType;
import se.transmode.tnm.model.nodes.snmp.SnmpAuthProtocol;
import se.transmode.tnm.model.nodes.snmp.SnmpContactDetails;
import se.transmode.tnm.model.nodes.snmp.SnmpPrivacyProtocol;
import se.transmode.tnm.model.nodes.snmp.SnmpVersion;
import se.transmode.tnm.mtosi.model.enums.LagActive;
import se.transmode.tnm.mtosi.model.enums.vendorext.IpTableStatus;
import se.transmode.tnm.rmiclient.server.services.discovery.NodeEntry;
import se.transmode.tnm.rmiclient.server.services.discovery.NodeEntryFactory;
import se.transmode.tnm.rmiclient.server.services.discovery.SubnetEntry;

import java.util.Collections;

class DnamNodeApiUtil {
    static NodeEntry getDefaultNodeEntry(String nodeIp) {


        SnmpContactDetails snmpContactDetails = SnmpContactDetails.useSpecified(
                SnmpContactDetails.SNMP_PORT_UNDEFINED,
                SnmpVersion.V3,
                60,
                "public",
                SnmpAuthProtocol.MD5,
                "oper",
                PasswordEncrypter.encrypt("1234567890", true),
                SnmpPrivacyProtocol.None,
                ""
        );

        SubnetEntry defaultSubnet = SubnetEntry.createSubnet(-1, "Network", 0, 0, null);
        defaultSubnet.setId(1);
        defaultSubnet.setType(1);

        return NodeEntryFactory.createSnmpContacted(
                "",
                "",
                NodeRef.ipAddress(nodeIp),
                NodeFamily.TM_3000,
                SubrackType.Subrack_Tm3000,
                AlarmHandling.EnableNodeAlarms,
                TopologyUsed.UseTopology,
                "",
                "",
                10,
                snmpContactDetails,
                FtpLoginDetails.EMPTY,
                EnmLoginDetails.EMPTY,
                "Network",
                Collections.singletonList(defaultSubnet),
                "",
                "",
                IpTableStatus.UNKNOWN,
                "",
                "",
                LagActive.ISNOTACTIVE
        );
    }
}
