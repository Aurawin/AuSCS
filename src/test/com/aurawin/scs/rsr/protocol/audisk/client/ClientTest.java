package com.aurawin.scs.rsr.protocol.audisk.client;

import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.rsr.protocol.transport.AUDISKTest;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.junit.Assert.*;

public class ClientTest extends com.aurawin.core.rsr.client.Client {
    protected InetSocketAddress RemoteIp;

    public ClientTest(InetSocketAddress bindIp, InetSocketAddress remoteIp) throws IOException, NoSuchMethodException,
            InstantiationException,IllegalAccessException
    {
        super(bindIp, AUDISKTest.class, true);
        RemoteIp= remoteIp;
    }
}