package com.aurawin.scs.stored.bootstrap;

import com.aurawin.core.solution.Namespace;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.ContentType;
import com.aurawin.scs.stored.DNS;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;
import org.junit.Test;

import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;
import static com.aurawin.core.stored.entities.Entities.CascadeOn;

/**
 * Created by atbrunner on 3/7/17.
 */

public class BootstrapTest {
    public static Disk dPhoenix;
    public static Domain domain;
    public static Location lc;
    public static Location lcDelete;
    public static Group gp;
    public static Group gpDelete;
    public static Resource rcPhoenix;
    public static Resource rcChump;
    public static Resource rcDelete;
    public static Node nPhoenix;
    public static Node nChump;
    public static Node nAu1;
    public static Node nAu2;
    public static Node nDisk;
    public static Node nHTTP;
    public static Node nDelete;
    public static DNS  dnsPhoenix;

    public static Account account;
    public static Account aDelete;
    public static Service svcHTTP;
    public static Service svcDelete;
    public static Service svcAUDISK;
    public static ArrayList<DNS> Servers;
    public static Disk auDisk;
    public static Certificate  Cert ;
    public static final String Mount = "/Developer/test";
    public static final String DomainName = "aurawin.com";

    public static Certificate createCertificate() throws Exception{
        return Certificate.createSelfSigned(
                "aurawin.com",
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
    }

    public static void createTestData() throws Exception {
        ContentType.installDefaults();

        Cert = Certificate.createSelfSigned(
                "aurawin.com",
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
        Entities.Save(Cert, CascadeOff);

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
        lcDelete = Bootstrap.Cloud.addLocation(
                "Falcon Pointe New",
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
        gpDelete= Bootstrap.Cloud.addGroup(
            lcDelete,
            "Office",
            "Primary",
            "Primary"
        );
        dnsPhoenix=Bootstrap.addDNS("172.16.1.1");
        Servers = Entities.Settings.DNS.listAll();
        rcPhoenix = Bootstrap.Cloud.addResource(gp,"Phoenix");
        rcChump = Bootstrap.Cloud.addResource(gp,"Chump");
        nPhoenix = Bootstrap.Cloud.addNode(rcPhoenix,"phoenix","172.16.1.1");
        nChump = Bootstrap.Cloud.addNode(rcChump,"chump","172.16.1.2");

        nAu1 = Bootstrap.Cloud.addNode(rcPhoenix,"au1","107.218.165.193");
        nAu2 = Bootstrap.Cloud.addNode(rcChump,"au2","107.218.165.194");
        nDelete= Bootstrap.Cloud.addNode(rcChump,"nDelete","172.16.54.1");


        nHTTP = nPhoenix;
        nDisk = nPhoenix;

        svcAUDISK = Bootstrap.Cloud.addService(
                nDisk,
                Namespace.Entities.getUniqueId(com.aurawin.scs.stored.cloud.Disk.class),
                Settings.Stored.Cloud.Service.Port.AuDisk,
                1,
                10,
                1
        );
        svcHTTP = Bootstrap.Cloud.addService(
                nHTTP,
                Namespace.Entities.getUniqueId(com.aurawin.scs.stored.cloud.service.HTTP.class),
                1080,
                1,
                10,
                1
        );
        svcDelete = Bootstrap.Cloud.addService(
                nDelete,
                Namespace.Entities.getUniqueId(com.aurawin.scs.stored.cloud.service.HTTP.class),
                80,
                1,
                10,
                1
        );
        gp = Entities.Lookup(Group.class, 1l);
        gp = Entities.Lookup(Group.class, 1l);

        auDisk = Bootstrap.Cloud.addDisk(nDisk,svcAUDISK,Mount);
        AuDisk.Initialize(nDisk,Cert);

        Disk dq = Entities.Cloud.Disk.byService(svcAUDISK);

        assert((dq!=null) && (dq.getId()==auDisk.getId()));

        domain = Bootstrap.addDomain(DomainName);
        account = Bootstrap.addUser(
                domain,
                "test",
                "1Bl4H4uotT",
                Namespace.Entities.getUniqueId(com.aurawin.scs.stored.security.role.User.class)
        );
        aDelete = Bootstrap.addUser(
                domain,
                "delete",
                "test",
                Namespace.Entities.getUniqueId(com.aurawin.scs.stored.security.role.User.class)
        );

        nPhoenix.setDomain(domain);
        nChump.setDomain(domain);
        nAu1.setDomain(domain);
        nAu2.setDomain(domain);


    }
}
