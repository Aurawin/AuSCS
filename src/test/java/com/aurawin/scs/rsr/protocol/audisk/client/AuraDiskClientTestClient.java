package com.aurawin.scs.rsr.protocol.audisk.client;

import com.aurawin.core.rsr.transport.annotations.Protocol;
import com.aurawin.scs.rsr.protocol.audisk.def.version.Version;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.rsr.protocol.transport.AUDISKTest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

import static org.junit.Assert.*;


public class AuraDiskClientTestClient extends com.aurawin.core.rsr.client.Client {

    public AuraDiskClientTestClient(InetSocketAddress bindIp) throws IllegalAccessException,
            InstantiationException,            NoSuchMethodException,IOException,InvocationTargetException

    {
        super(bindIp, AUDISKTest.class, true);
    }
}