package com.aurawin.scs.rsr.protocol.audisk.client;

import com.aurawin.core.Environment;
import com.aurawin.core.lang.Database;
import com.aurawin.core.lang.Table;
import com.aurawin.core.rsr.def.CertSelfSigned;
import com.aurawin.core.rsr.def.EngineState;
import com.aurawin.core.rsr.def.ItemState;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.stored.entities.Certificate;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cDeleteFolder;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cReadFile;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cWriteFile;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Settings;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.lang.Namespace;
import com.aurawin.scs.rsr.protocol.audisk.server.Server;
import com.aurawin.scs.stored.bootstrap.Bootstrap;
import com.aurawin.scs.stored.bootstrap.BootstrapTest;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.KeySpec;

import static com.aurawin.core.rsr.def.ItemState.isEstablished;
import static org.junit.Assert.*;


public class AuraDiskClientTest {
    boolean issued = false;
    Client Connector;
    InetSocketAddress saServer  = new InetSocketAddress("172.16.1.1",Settings.Stored.Cloud.Service.Port.AuDisk);
    InetSocketAddress saClient  = new InetSocketAddress("172.16.1.2",0);
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

        Connector = new Client(saClient,saServer);
        Certificate cert = new Certificate();
        CertSelfSigned ssc = new CertSelfSigned(
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
        cert.Request=Table.Security.Certificate.Request.SelfSigned;
        cert.DerKey=ssc.getPrivateKeyAsDER();
        cert.TextKey=ssc.PrintPrivateKey();

        cert.DerCert1=ssc.getCertificateAsDER();
        cert.TextCert1 = ssc.PrintCertificate();

        cert.ChainCount=1;
        cert.Expires=ssc.ToDate.toInstant();

        Connector.SSL.Load(cert);
        Connector.SSL.Enabled=true;
        Connector.Configure();
    }

    @Test
    public void test() throws Exception{

        System.out.println("AuraDiskClientTest.testRun()");
        System.out.println("AuraDiskClientTest.Server.Start()");
        Connector.Start();
        System.out.println("AuDisk Client is running");

        AUDISK rsr = Connector.Connect(saServer);

        while (Connector.State != EngineState.esFinalize) {
            if (rsr.State== isEstablished) {
                if (!issued) {
                    issued = true;
                    cReadFile cmd = new cReadFile();

                    cmd.DiskId = 1;
                    cmd.DomainId = 1;
                    cmd.FileId = 1221312;
                    cmd.FolderId = 13342300;
                    cmd.NamespaceId = 1;
                    cmd.OwnerId = 1234;

                    rsr.Request.Protocol = rsr.Version.toString();
                    rsr.Request.Method = cmd.Keys.get(0);
                    rsr.Request.Command = rsr.gson.toJson(cmd);


                    rsr.Query();
                } else {

                }
                Thread.sleep(100);
            }
        }
    }
}