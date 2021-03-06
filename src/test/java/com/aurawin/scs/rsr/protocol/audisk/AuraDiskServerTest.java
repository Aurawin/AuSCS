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
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.rsr.protocol.audisk.server.Server;
import com.aurawin.scs.stored.bootstrap.Bootstrap;
import com.aurawin.scs.stored.bootstrap.BootstrapTest;

import com.aurawin.scs.stored.cloud.Disk;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuraDiskServerTest {
    public static final String basePackage = "com.aurawin";
    Server Server;
    @Before
    public void before() throws Exception{
        Settings.Initialize(
                "AuProcess",
                "Aurawin LLC",
                "Aurawin Social Computing Server",
                "Universal"
        );
        Manifest mf = new Manifest (
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
                Database.Config.Automatic.Update,                               // Update or Install
                "aus",                                          // database
                Dialect.Postgresql.getValue(),                                  // Dialect
                Driver.Postgresql.getValue(),                                   // Driver
                Bootstrap.buildAnnotations(com.aurawin.core.Package.class,com.aurawin.scs.Package.class)
        );
        Entities.Initialize(mf);

        BootstrapTest.dPhoenix = Entities.Lookup(Disk.class,1l);
        Certificate cert = BootstrapTest.createCertificate();
        BootstrapTest.nPhoenix=Entities.Lookup(Node.class,1l);
        BootstrapTest.svcAUDISK=Entities.Lookup(Service.class,BootstrapTest.dPhoenix.getServiceId());

        AuDisk.Initialize(BootstrapTest.nPhoenix,cert);

        Server = new Server(mf,BootstrapTest.svcAUDISK);
        Server.loadSecurity(cert);


        Server.Configure();
    }

    @Test
    public void testAuDiskServer() throws Exception
    {

        System.out.println("AuraDiskServerTest.testRun()");
        System.out.println("AuraDiskServerTest.Server.Start()");
        Server.Start();
        System.out.println("AuDisk Server is running");

        while (Server.State != EngineState.esStop) {

            Thread.sleep(100);
        }
        System.out.println("AuDisk Server shut down");
    }
    @After
    public void after() throws Exception {
        Server.Stop();
        Server=null;
    }

}
