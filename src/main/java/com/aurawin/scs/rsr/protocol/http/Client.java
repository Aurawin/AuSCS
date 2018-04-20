package com.aurawin.scs.rsr.protocol.http;

import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

public class Client extends com.aurawin.core.rsr.client.Client {
    protected InetSocketAddress RemoteIp;

    public Client(InetSocketAddress bindIp, InetSocketAddress remoteIp) throws InvocationTargetException,IOException, NoSuchMethodException,
            InstantiationException,IllegalAccessException
    {
        super(bindIp, HTTP_1_1.class, true);
        RemoteIp= remoteIp;
    }




}