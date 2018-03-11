package com.aurawin.scs.stored.bootstrap;

import com.aurawin.core.lang.Table;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.lang.Namespace;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;
import static com.aurawin.core.stored.entities.Entities.CascadeOff;

/**
 * Created by atbrunner on 3/7/17.
 */
public class BootstrapTest {
    public static Domain domain;
    public static Location lc;
    public static Group gp;
    public static Resource rcPhoenix;
    public static Resource rcChump;
    public static Node nPhoenix;
    public static Node nChump;
    public static Node nAu1;
    public static Node nAu2;

    public static Account account;
    public static Service svcHTTP;
    public static Service svcAUDISK;
    public static Disk auDisk;
    public static final String Mount = "/media/raid";
    public static final String DomainName = "aurawin.com";

    public static void createTestData() throws Exception {

        lc = Bootstrap.Cloud.addLocation(
                "Falcon Pointe",
                "19309",
                "Stage Line Trail",
                "1st",
                "Office",
                "Pflugerville",
                "Texas",
                "78660",
                "USA"
        );
        gp = Bootstrap.Cloud.addGroup(
                lc,
                "Office",
                "Primary",
                "Primary"
        );
        rcPhoenix = Bootstrap.Cloud.addResource(gp,"Phoenix");
        rcChump = Bootstrap.Cloud.addResource(gp,"Chump");
        nPhoenix = Bootstrap.Cloud.addNode(rcPhoenix,"phoenix","172.16.1.1");
        nChump = Bootstrap.Cloud.addNode(rcChump,"chump","172.16.1.2");
        nAu1 = Bootstrap.Cloud.addNode(rcPhoenix,"au1","107.218.165.193");
        nAu2 = Bootstrap.Cloud.addNode(rcChump,"au2","107.218.165.194");
        AuDisk.Initialize(nChump);

        svcAUDISK = Bootstrap.Cloud.addService(
                nPhoenix,
                Namespace.Stored.Cloud.Service.AUDISK,
                Settings.Stored.Cloud.Service.Port.AuDisk,
                1,
                10,
                1
        );
        svcHTTP = Bootstrap.Cloud.addService(
                nPhoenix,
                Namespace.Stored.Cloud.Service.HTTP,
                1080,
                1,
                10,
                1
        );
        auDisk = Bootstrap.Cloud.addDisk(nPhoenix,svcAUDISK,Mount);

        domain = Bootstrap.addDomain(DomainName);
        account=Bootstrap.addUser(domain,"test","1Bl4H4uotT");

        nPhoenix.setDomain(domain);
        nChump.setDomain(domain);
        nAu1.setDomain(domain);
        nAu2.setDomain(domain);


        Certificate  cert = Certificate.createSelfSigned(
                "phoenix.aurawin.com",
                "NOC",
                "Aurawin LLC",
                "19309 Stage Line Trail",
                "Pflugerville",
                "TX",
                "78660",
                "US",
                "support@aurawin.com",
                365
        );

        Entities.Save(cert, CascadeOff);
    }
}
