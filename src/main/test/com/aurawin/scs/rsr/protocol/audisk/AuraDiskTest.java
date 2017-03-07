package com.aurawin.scs.rsr.protocol.audisk;

import com.aurawin.core.Environment;
import com.aurawin.core.lang.Database;
import com.aurawin.core.lang.Table;
import com.aurawin.core.rsr.def.EngineState;
import com.aurawin.core.solution.Settings;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.lang.Namespace;

import com.aurawin.scs.stored.bootstrap.Bootstrap;
import com.aurawin.scs.stored.bootstrap.Stored;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.UserAccount;
import com.aurawin.scs.stored.domain.network.Folder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;
import static com.aurawin.core.stored.entities.Entities.CascadeOn;

public class AuraDiskTest {
    com.aurawin.scs.rsr.protocol.audisk.server.Server Server;
    Manifest Manifest;
    @Before
    public void before() throws Exception{
        Settings.Initialize(
                "AuProcess",
                "Aurawin Social Computing Server",
                "Universal",
                "2017",
                "2",
                "28"
        );
        Manifest = new Manifest (
                Environment.getString(Table.DBMS.Username),                     // username
                Environment.getString(Table.DBMS.Password),                     // password
                Environment.getString(Table.DBMS.Host),                         // host
                Environment.getInteger(Table.DBMS.Port),                        // port
                com.aurawin.core.lang.Database.Config.Automatic.Commit.On,      // autocommit
                2,                                                   // Min Poolsize
                20,                                                 // Max Poolsize
                1,                                                 // Pool Acquire Increment
                50,                                               // Max statements
                10,                                                     // timeout
                Database.Config.Automatic.Create,                               // Update or Install
                "AuDiskTest",                                          // database
                Dialect.Postgresql.getValue(),                                  // Dialect
                Driver.Postgresql.getValue(),                                   // Driver
                Bootstrap.buildAnnotations()
        );
        Namespace.Merge(Manifest.Namespaces);
        Entities.Initialize(Manifest);
    }
    @Test
    public void testPopulateDatabase() throws Exception
    {

        Location location = new Location();
        location.setCountry("US");
        location.setLocality("Pflugerville");
        location.setRegion("Texas");
        location.setZip("78660");
        location.setArea("Falcon Pointe");
        location.setBuilding("19309");
        location.setFloor("1");
        location.setRoom("Office");
        location.setStreet("19309 Stage Line Trail");

        Entities.Save(location,CascadeOn);

        Group g = new Group();
        g.setName("Office");
        g.setLocation(location);
        Entities.Save(g,CascadeOn);

        Resource phoenix = new Resource();
        phoenix.setName("phoenix");
        phoenix.setGroup(g);
        Entities.Save(phoenix,CascadeOn);

        Node n1 = new Node();
        n1.setName("phoenix.aurawin.com");
        n1.setIP("172.16.1.1");
        n1.setGroup(g);
        n1.setResource(phoenix);
        Entities.Save(n1,CascadeOn);


        Resource chump = new Resource();
        phoenix.setName("chump");
        phoenix.setGroup(g);
        Entities.Save(chump,CascadeOn);

        Node n2 = new Node();
        n2.setName("chump.aurawin.com");
        n2.setIP("172.16.1.2");
        n2.setGroup(g);
        n2.setResource(chump);
        Entities.Save(n2,CascadeOn);

        Disk d = new Disk();
        d.setOwnerId(n2.getId());
        d.setMount("/media/raid");
        Entities.Save(d,CascadeOn);

        Domain dChump = new Domain("chump.aurawin.com",Table.String(Table.Entities.Domain.Root));
        dChump.setFriendlyName("Chump");
        Entities.Save(dChump,CascadeOn);

        n2.setDomain(dChump); // bind chump to node ip
        Entities.Update(n2,CascadeOff);

    }
    @Test
    public void testAuDiskMakeDirectory() throws Exception {
        Node node = Server.getNode();

        Domain d = node.getDomain();
        if ((d)==null) {
            d = Entities.Lookup(Domain.class,"chump.aurawin.com");
            node.setDomain(d);
            Entities.Update(node, CascadeOff);
        }
        UserAccount root = Entities.Lookup(UserAccount.class, d.getId(),d.getRoot());
        Folder Home = Entities.Lookup(Folder.class,d.getId(),"");
        //AuDisk.makeDirectory();
    }
    @Test
    public void testAuDiskServer() throws Exception
    {
        AuDisk.Initialize(Manifest,2l);
        Server = new com.aurawin.scs.rsr.protocol.audisk.server.Server(Manifest,2l);
        Server.Configure();

        System.out.println("AuraDiskTest.testRun()");
        System.out.println("AuraDiskTest.Server.Start()");
        Server.Start();
        System.out.println("AuDisk is running");
        while (Server.State != EngineState.esFinalize) {
            Thread.sleep(100);
        }
    }
    @After
    public void after() throws Exception {
        Server.Stop();
        Server=null;
    }

}
