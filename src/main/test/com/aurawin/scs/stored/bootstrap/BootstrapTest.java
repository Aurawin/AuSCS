package com.aurawin.scs.stored.bootstrap;

import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.lang.Namespace;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;

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
    public static Account account;
    public static Service svcHTTP;
    public static Service svcAUDISK;
    public static Disk auDisk;
    public static final String Mount = "/media/raid";
    public static final String DomainName = "chump.aurawin.com";

    public static void createTestData() throws Exception {
        Bootstrap.Initialize();
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
        AuDisk.Initialize(nChump);

        svcAUDISK = Bootstrap.Cloud.addService(
                nChump,
                Namespace.Stored.Cloud.Service.AUDISK,
                1081,
                1,
                10,
                1
        );
        svcHTTP = Bootstrap.Cloud.addService(
                nChump,
                Namespace.Stored.Cloud.Service.HTTP,
                1080,
                1,
                10,
                1
        );
        auDisk = Bootstrap.Cloud.addDisk(nChump,svcAUDISK,Mount);

        domain = Bootstrap.addDomain(DomainName);
        account=Bootstrap.addUser(domain,"test","1Bl4H4uotT");
        nChump.setDomain(domain);


    }
}
