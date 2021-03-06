package com.aurawin.scs.stored;

import com.aurawin.core.Environment;
import com.aurawin.core.lang.Table;
import com.aurawin.core.solution.Namespace;
import com.aurawin.core.solution.Settings;
import com.aurawin.core.stored.entities.FetchKind;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.lang.Database;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.scs.stored.bootstrap.Bootstrap;
import com.aurawin.scs.stored.bootstrap.BootstrapTest;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.KeyValue;
import com.aurawin.scs.stored.domain.network.File;
import com.aurawin.scs.stored.domain.network.Folder;
import com.aurawin.scs.stored.domain.user.Roster;
import com.aurawin.scs.stored.domain.user.Account;
import com.aurawin.scs.stored.domain.network.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.time.Instant;
import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.*;

public class EntitiesTest {
    public static final String DomainName = "chump.aurawin.com";
    Manifest Manifest;

    @Before
    public void before() throws Exception {
        Settings.Initialize(
                "AuProcess",
                "Aurawin LLC",
                "Aurawin Social Computing Server",
                "Universal"
        );
        Manifest=new Manifest(
                Environment.getString(Table.DBMS.Username), // username
                Environment.getString(Table.DBMS.Password),  // password
                Environment.getString(Table.DBMS.Host),     // host
                Environment.getInteger(Table.DBMS.Port),     // port
                Database.Config.Automatic.Commit.On,    // autocommit
                2,                                      // Min Poolsize
                20,                                     // Max Poolsize
                1,                                      // Pool Acquire Increment
                50,                                     // Max statements
                10,                                     // timeout
                Database.Config.Automatic.Create,       //
                "EntitiesTest",                                 // database
                Dialect.Postgresql.getValue(),          // Dialect
                Driver.Postgresql.getValue(),            // Driver
                Bootstrap.buildAnnotations(com.aurawin.core.Package.class,com.aurawin.scs.Package.class)
        );
        Entities.Initialize(Manifest);

    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testDomainDelete() throws Exception{
        Domain vD = Entities.Lookup(Domain.class,DomainName);
    }
    @Test
    public void testCheckEntitiesAsCreate() throws Exception {
        BootstrapTest.createTestData();

        File f = new File();
        f.setCreated(Instant.now());
        f.setModified(Instant.now());
        f.setFolderId(BootstrapTest.aDelete.Cabinet.Root.getId());
        f.setDiskIdId(BootstrapTest.aDelete.Cabinet.getDiskId());
        f.setOwnerId(BootstrapTest.aDelete.getId());
        f.setDomainId(BootstrapTest.aDelete.getDomainId());
        f.setNetworkId(BootstrapTest.aDelete.Cabinet.getId());
        f.setName("Test.txt");
        f.setSummary("This is a test file.");

        Entities.Save(f,CascadeOn);

        MemoryStream ms = new MemoryStream();
        ms.Write("This is a test of content");
        AuDisk.writeFile(
                ms,
                f.getDiskId(),
                Namespace.Entities.Identify(File.class),
                f.getDomainId(),
                f.getOwnerId(),
                f.getFolderId(),
                f.getId()
        );

        Entities.Delete(BootstrapTest.svcDelete,CascadeOn,UseNewTransaction);
        Entities.Delete(BootstrapTest.nDelete,CascadeOn,UseNewTransaction);


        Entities.Purge(BootstrapTest.aDelete,CascadeOn);
        Entities.Delete(BootstrapTest.aDelete,CascadeOn,UseNewTransaction);

        Certificate cert = Entities.Lookup(Certificate.class,1l);
        AuDisk.Initialize(BootstrapTest.nChump,cert);

        ArrayList<Domain> ds = Entities.Domains.listAll();

        Domain lD = Entities.Lookup(Domain.class,BootstrapTest.domain.getId());
        Node  lN = Entities.Lookup(Node.class,2l);
        ArrayList<Service> svcs = Entities.Cloud.Service.listAll(lN);

        KeyValue kv = new KeyValue(
                lD.getId(),
                Namespace.Entities.Identify(KeyValue.class),
                "testKeyword"
        );
        kv.setValue("This is a test");
        Entities.Save(kv,CascadeOff);
        ArrayList<KeyValue> list = Entities.Domains.KeyValues.listAll(lD);



        Account lUA = Entities.Lookup(Account.class,lD.getId(), lD.Root.getId());
        Entities.Fetch(lUA, FetchKind.Infinite);


        Roster r = new Roster(lUA,"Bestie");
        r.setAddresses("19309 Stage Line Trl.");
        r.setFirstName("Jax");
        r.setMiddleName("");
        r.setFamilyName("Brunner");
        r.setCity("Austin");
        r.setState("TX");
        r.setPostal("78660");
        Entities.Save(r,CascadeOn);

        ArrayList<Account> uaAll=Entities.Domains.Users.listAll(lD);


        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();

        String jsUA = gson.toJson(lUA);
        String jsD = gson.toJson(lD);

        Network lCAB = lUA.Cabinet;
        Roster lME = lUA.Me;

    }


} 
