package com.aurawin.scs.rsr.protocol.audisk.client;

import com.aurawin.core.lang.Table;
import com.aurawin.core.rsr.def.EngineState;
import com.aurawin.core.rsr.def.TransportConnect;
import com.aurawin.core.solution.Version;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.rsr.protocol.audisk.client.AuraDiskClientTestClient;


import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;


public class AuraDiskClientTest {
    TransportConnect tcData;
    AuraDiskClientTestClient Engine;
    InetSocketAddress saServer  = new InetSocketAddress("172.16.1.1",Settings.Stored.Cloud.Service.Port.AuDisk);
    InetSocketAddress saClient  = new InetSocketAddress("172.16.1.2",0);
    @Before
    public void before() throws Exception{
        Settings.Initialize(
                "AuProcess",
                "Aurawin Social Computing Server",
                "Universal"
        );

        Engine = new AuraDiskClientTestClient(saClient,saServer);
        Certificate cert = Certificate.createSelfSigned(
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
        Engine.SSL.Load(cert);
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

        while (Engine.State != EngineState.esFinalize) {

            Thread.sleep(100);
        }
    }
}