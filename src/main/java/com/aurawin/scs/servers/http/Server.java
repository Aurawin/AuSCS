package com.aurawin.scs.servers.http;

import com.aurawin.core.rsr.def.ItemKind;
import com.aurawin.scs.servers.protocol.HTTP_1_1;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server extends com.aurawin.core.rsr.server.Server{

    public Server(InetSocketAddress sa,  String aHostName) throws IOException, NoSuchMethodException,
            InstantiationException,IllegalAccessException
    {
        super(sa, HTTP_1_1.class, false, aHostName);
    }
}
