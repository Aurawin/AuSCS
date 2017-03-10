package com.aurawin.scs.rsr.protocol.audisk.client;


import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Client extends com.aurawin.core.rsr.client.Client{
    protected InetSocketAddress RemoteIp;
    public Client(InetSocketAddress bindIp, InetSocketAddress remoteIp) throws IOException, NoSuchMethodException,
            InstantiationException,IllegalAccessException
    {
        super(bindIp, AUDISK.class, true);
        RemoteIp= remoteIp;
    }
}
