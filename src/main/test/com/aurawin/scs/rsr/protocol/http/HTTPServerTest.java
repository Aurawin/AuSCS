package com.aurawin.scs.rsr.protocol.http;

import com.aurawin.core.Environment;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.entities.FetchKind;
import com.aurawin.scs.lang.Namespace;
import com.aurawin.core.lang.Table;

import com.aurawin.core.rsr.def.EngineState;
import com.aurawin.core.solution.Settings;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.scs.core.Login;
import com.aurawin.scs.core.Noid;
import com.aurawin.scs.stored.bootstrap.Bootstrap;
import com.aurawin.scs.stored.bootstrap.Stored;
import com.aurawin.scs.stored.domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.InetSocketAddress;

public class HTTPServerTest {
    com.aurawin.scs.rsr.protocol.http.Server Server;
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
                com.aurawin.core.lang.Database.Config.Automatic.Update,         //
                "HTTPServerTest",                                      // database
                Dialect.Postgresql.getValue(),                                  // Dialect
                Driver.Postgresql.getValue(),                                   // Driver
                Bootstrap.buildAnnotations()
        );
        Namespace.Merge(mf.Namespaces);
        Entities.Initialize(mf);

        Server = new Server(
                new InetSocketAddress("172.16.1.2", 1080),
                "chump.aurawin.com"
        );




        Server.Domain = Entities.Lookup(Domain.class,1l);
        Server.Root = Entities.Lookup(UserAccount.class,Server.Domain.getRootId());
        Entities.Fetch(Server.Root, FetchKind.Infinite);

        // Testing without https
        // Server.loadSecurity(1l);

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