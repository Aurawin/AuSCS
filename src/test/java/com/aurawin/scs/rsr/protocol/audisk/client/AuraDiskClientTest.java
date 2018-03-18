package com.aurawin.scs.rsr.protocol.audisk.client;

import com.aurawin.core.Environment;
import com.aurawin.core.json.Builder;
import com.aurawin.core.lang.Database;
import com.aurawin.core.lang.Table;
import com.aurawin.core.rsr.def.EngineState;
import com.aurawin.core.rsr.def.TransportConnect;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.rsr.protocol.audisk.def.Response;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cListFiles;
import com.aurawin.scs.solution.Namespace;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cMoveFile;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Settings;


import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.bootstrap.Bootstrap;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.io.Console;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import static com.aurawin.core.rsr.transport.methods.Result.Ok;


public class AuraDiskClientTest {
    String sJSON;
    cListFiles cmdListFiles;
    TransportConnect tcData;
    Builder bldr;
    Gson gson;
    UniqueId Kind;
    AUDISK t;
    AuraDiskClientTestClient Engine;
    InetSocketAddress saServer  = new InetSocketAddress("172.16.1.1",Settings.Stored.Cloud.Service.Port.AuDisk);
    InetSocketAddress saClient  = new InetSocketAddress("172.16.1.2",0);
    @Before
    public void before() throws Exception{
        bldr = new Builder();
        gson = bldr.Create();


        Kind= Namespace.Stored.Domain.Network.File;
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
                Bootstrap.buildAnnotations()
        );

        Namespace.Merge(mf.Namespaces);
        com.aurawin.core.stored.entities.Entities.Initialize(mf);

        Engine = new AuraDiskClientTestClient(saClient,saServer);
        Certificate cert = Entities.Lookup(Certificate.class,1l);
        Engine.SSL.Load(cert);
        Engine.Infinite=true;

        Engine.SSL.Enabled=true;
        Engine.Configure();
    }

    @Test
    public void test() throws Exception{

        System.out.println("AuraDiskClientTest.testRun()");
        System.out.println("AuraDiskClientTest.Server.Start()");
        Engine.Start();
        System.out.println("AuDisk Client is running");

        tcData=Engine.Connect(saServer,true);

        while (
                (Engine.State != EngineState.esFinalize) &&
                (tcData.isAlive()==true)
        ){
            if (tcData.readyForUse()) {
                AUDISK T = (AUDISK) tcData.getOwnerOrWait();
                if (cmdListFiles==null){
                    t = (AUDISK) tcData.getOwnerOrWait();
                    cmdListFiles = new cListFiles();
                    cmdListFiles.DiskId=1;
                    cmdListFiles.DomainId=1;
                    cmdListFiles.NamespaceId=Kind.getId();
                    cmdListFiles.FolderId=1;
                    cmdListFiles.OwnerId=1;

                    Response r = t.Query(cmdListFiles,null);
                    if (r.Code==Ok){
                        String[] result = t.gson.fromJson(r.Payload.toString(),String[].class);
                        System.out.println("Results:".concat(result.toString()));
                    } else{
                        throw new Exception("Disk [Move] command failed.");
                    }

                }
            }
            Thread.sleep(100);
        }
    }
}