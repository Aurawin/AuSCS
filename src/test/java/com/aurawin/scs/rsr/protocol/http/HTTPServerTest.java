package com.aurawin.scs.rsr.protocol.http;

import com.aurawin.core.Environment;
import com.aurawin.core.lang.Database;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.core.lang.Table;

import com.aurawin.core.rsr.def.EngineState;
import com.aurawin.core.solution.Settings;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.scs.core.Login;
import com.aurawin.scs.core.Noid;
import com.aurawin.scs.stored.bootstrap.Bootstrap;
import com.aurawin.scs.stored.bootstrap.BootstrapTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HTTPServerTest {
    public static final String basePackage = "com.aurawin";

    com.aurawin.scs.rsr.protocol.http.Server Server;
    @Before
    public void before() throws Exception{
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
                Database.Config.Automatic.Create,                               //
                "HTTPServerTest",                                      // database
                Dialect.Postgresql.getValue(),                                  // Dialect
                Driver.Postgresql.getValue(),                                   // Driver
                Bootstrap.buildAnnotations(basePackage)
        );

        Entities.Initialize(mf);

        com.aurawin.scs.rsr.protocol.http.Server.Bootstrap();

        BootstrapTest.createTestData();

        Certificate cert = com.aurawin.scs.stored.Entities.Lookup(Certificate.class,1l);


        Server = new Server(mf,BootstrapTest.svcHTTP);

        // Testing without https
        Server.SSL.Load(cert);
        Server.installPlugin(new Noid());
        Server.installPlugin(new Login());
        Server.Configure();


    }
    @Test
    public void testServer() throws Exception
    {
        System.out.println("HTTPServerTest.testRun()");
        System.out.println("HTTPServerTest.Server.Start()");
        Server.Start();
        System.out.println("HTTPServerTest.serverHTTP running");
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