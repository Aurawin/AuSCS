package com.aurawin.scs.rsr.protocol.audisk.client;

import com.aurawin.core.Environment;
import com.aurawin.core.json.Builder;
import com.aurawin.core.lang.Database;
import com.aurawin.core.lang.Table;
import com.aurawin.core.rsr.IpHelper;
import com.aurawin.core.rsr.Item;
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

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Random;

import static com.aurawin.core.lang.Table.CRLF;


public class AuraDiskClientTest {
    final String alphabet = "abcdefghigjklmnopqrstuvwzyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    final int len = alphabet.length();
    final Random r = new Random();
    String line = "";
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
    Builder bldr;
    Gson gson;
    long Kind;

    @Before
    public void before() throws Exception {
        bldr = new Builder();
        gson = bldr.Create();
        Input = new MemoryStream();
        Output = new MemoryStream();
        for (int jLcv = 1; jLcv <= 1024*2; jLcv++) {
            line += alphabet.charAt(r.nextInt(len));
        }
        for (int iLcv = 1; iLcv <= 1024*720; iLcv++){

            Input.Write(line + " " + iLcv + CRLF);
        }



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
                "aus",                                      // database
                Dialect.Postgresql.getValue(),                                  // Dialect
                Driver.Postgresql.getValue(),                                   // Driver
                Bootstrap.buildAnnotations(com.aurawin.core.Package.class,com.aurawin.scs.Package.class)
        );
        Entities.Initialize(mf);
        Kind = Namespace.Entities.Identify(com.aurawin.scs.stored.domain.network.File.class);


        nServer = Entities.Lookup(Node.class,1l);
        nClient = Entities.Lookup(Node.class,2l);

        Certificate cert = Certificate.createSelfSigned(
                "*",
                "*",
                "*",
                "*",
                "*",
                "*",
                "*",
                "*",
                "*",
                365
        );

        AuDisk.Initialize(nClient,cert);

    }

    @Test
    public void test() throws Exception{
        System.out.println("AuraDiskClientTest.testRun()");


        if (AuDisk.makeFolder(DiskId,Kind,DomainId,OwnerId,FolderId)) {
            System.out.println("AuDisk makeFolder completed");
            if (AuDisk.makeFile(DiskId, Kind, DomainId, OwnerId, FolderId, FileId)) {
                System.out.println("AuDisk makeFile completed");
                AuDisk.writeFile(Input, DiskId, Kind, DomainId, OwnerId, FolderId, FileId);
                System.out.println("AuDisk writeFile completed");
                AuDisk.readFile(Output, DiskId, Kind, DomainId, OwnerId, FolderId, FileId);
                System.out.println("AuDisk readFile completed");
                Output.SaveToFile(new File("/home/atbrunner/Desktop/check.txt"));
            } else {
                throw new Exception("AuDisk [makeFile] command failed.");
            }
        } else {
            throw new Exception("AuDisk [makeFolder] command failed.");
        }

/*        while (true){
            Thread.sleep(100);
        }*/
    }
}