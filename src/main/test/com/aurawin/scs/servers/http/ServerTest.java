package com.aurawin.scs.servers.http;

import com.aurawin.core.Environment;
import com.aurawin.core.VarString;
import com.aurawin.core.lang.Table;
import com.aurawin.core.plugin.Noid;
import com.aurawin.core.rsr.Engine;
import com.aurawin.core.rsr.def.ItemKind;
import com.aurawin.core.rsr.server.*;
import com.aurawin.core.rsr.transport.http_1_1;
import com.aurawin.core.solution.Settings;
import com.aurawin.core.stored.Dialect;
import com.aurawin.core.stored.Driver;
import com.aurawin.core.stored.Manifest;
import com.aurawin.lang.Database;
import com.aurawin.scs.stored.domain.UserAccount;
import com.google.gson.Gson;
import org.junit.Test;

import java.net.InetSocketAddress;

import static org.junit.Assert.*;

public class ServerTest {
    com.aurawin.scs.servers.http.Server Server;
    @Test
    public void testServer() throws Exception {
        Settings.Initialize("server.test");
        Manifest mf = Engine.createManifest(
                Environment.getString(Table.DBMS.Username), // username
                Environment.getString(Table.DBMS.Password),  // password
                Environment.getString(Table.DBMS.Host),     // host
                Environment.getInteger(Table.DBMS.Port),     // port
                com.aurawin.core.lang.Database.Config.Automatic.Commit.On,    // autocommit
                2,                                      // Min Poolsize
                20,                                     // Max Poolsize
                1,                                      // Pool Acquire Increment
                50,                                     // Max statements
                10,                                     // timeout
                com.aurawin.core.lang.Database.Config.Automatic.Update,       //
                "Test",                                 // database
                Dialect.Postgresql.getValue(),          // Dialect
                Driver.Postgresql.getValue()            // Driver
        );
        Server = new Server(new InetSocketAddress("172.16.1.2", 1080), "chump.aurawin.com");
        Server.setManifest(mf);
        Server.loadSecurity(1l);
        Server.installPlugin(new Noid());
        Server.Configure();
        Server.Start();
    }
}