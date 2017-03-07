package com.aurawin.scs.rsr.protocol.http;

import com.aurawin.scs.stored.domain.Domain;

import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.stored.domain.UserAccount;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server extends com.aurawin.core.rsr.server.Server{
    public Domain Domain;
    public UserAccount Root;
    public Server(InetSocketAddress sa,  String aHostName) throws IOException, NoSuchMethodException,
            InstantiationException,IllegalAccessException
    {
        super(sa, HTTP_1_1.class, false, aHostName);
    }
}
