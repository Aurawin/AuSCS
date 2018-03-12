package com.aurawin.scs.rsr.protocol.audisk.client;

import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.rsr.protocol.transport.AUDISKTest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

import static org.junit.Assert.*;

public class AuraDiskClientTestClient extends com.aurawin.core.rsr.client.Client {
    protected InetSocketAddress RemoteIp;

    public AuraDiskClientTestClient(InetSocketAddress bindIp, InetSocketAddress remoteIp) throws IllegalAccessException,
            InstantiationException,            NoSuchMethodException,IOException,InvocationTargetException

    {
        super(bindIp, AUDISKTest.class, true);
        RemoteIp= remoteIp;
    }
}