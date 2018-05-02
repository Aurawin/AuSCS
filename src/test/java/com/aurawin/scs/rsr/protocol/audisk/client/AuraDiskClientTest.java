package com.aurawin.scs.rsr.protocol.audisk.client;

import com.aurawin.core.Environment;
import com.aurawin.core.json.Builder;
import com.aurawin.core.lang.Database;
import com.aurawin.core.lang.Table;
import com.aurawin.core.rsr.IpHelper;
import com.aurawin.core.rsr.def.TransportConnect;
import com.aurawin.core.solution.Namespace;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cListFiles;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Settings;


import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.bootstrap.Bootstrap;
import com.aurawin.scs.stored.cloud.Node;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;


public class AuraDiskClientTest {
    public static final String basePackage = "com.aurawin";
    long DiskId=1;
    long DomainId=1;
    long OwnerId=1;
    long FolderId=1;
    long FileId = 1;
    MemoryStream Input;
    MemoryStream Output;
    Node nClient;
    Node nServer;
    String sJSON;
    cListFiles cmdListFiles;
    TransportConnect tcData;
    Builder bldr;
    Gson gson;
    long Kind;
    AUDISK t;
    AuraDiskClientTestClient Engine;
    String TestString;

    @Before
    public void before() throws Exception{
        bldr = new Builder();
        gson = bldr.Create();
        TestString  = "Testing";
        Input = new MemoryStream();
        Output = new MemoryStream();
        Input.Write(TestString);

        Kind = Namespace.Entities.Identify(com.aurawin.scs.stored.domain.network.File.class);
        Settings.Initialize(
                "AuProcess",
                "Aurawin Social Computing Server",
                "Universal"
        );
        Manifest mf = new Manifest(
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
                Database.Config.Automatic.Update,                               //
                "AuDiskTest",                                      // database
                Dialect.Postgresql.getValue(),                                  // Dialect
                Driver.Postgresql.getValue(),                                   // Driver
                Bootstrap.buildAnnotations(com.aurawin.core.Package.class,com.aurawin.scs.Package.class)
        );

        Entities.Initialize(mf);


        nServer = Entities.Lookup(Node.class,1l);
        nClient = Entities.Lookup(Node.class,2l);
        Certificate cert = Entities.Lookup(Certificate.class,1l);

        AuDisk.Initialize(nClient,cert);

        InetSocketAddress saBind  = new InetSocketAddress(IpHelper.fromLong(nClient.getIP()),Settings.RSR.AnyPort);

        Engine = new AuraDiskClientTestClient(saBind);

        Engine.SSL.Load(cert);
        Engine.Infinite=true;
        Engine.Configure();
    }

    @Test
    public void test() throws Exception{

        System.out.println("AuraDiskClientTest.testRun()");
        System.out.println("AuraDiskClientTest.Server.Start()");
        Engine.Start();
        System.out.println("AuDisk Client is running");

        InetSocketAddress saServer  = new InetSocketAddress(IpHelper.fromLong(nServer.getIP()),Settings.Stored.Cloud.Service.Port.AuDisk);

        tcData=Engine.Connect(saServer,true);

        if (AuDisk.makeFolder(DiskId,Kind,DomainId,OwnerId,FolderId)) {
            if (AuDisk.makeFile(DiskId, Kind, DomainId, OwnerId, FolderId, FileId)) {
                AuDisk.writeFile(Input, DiskId, Kind, DomainId, OwnerId, FolderId, FileId);
                AuDisk.readFile(Output, DiskId, Kind, DomainId, OwnerId, FolderId, FileId);
                assert(TestString.equals(Output.toString()));
            } else {
                throw new Exception("AuDisk [makeFile] command failed.");
            }

        } else {
            throw new Exception("AuDisk [makeFolder] command failed.");
        }
    }
}