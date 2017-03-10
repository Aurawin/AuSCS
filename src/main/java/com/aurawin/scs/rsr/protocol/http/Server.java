package com.aurawin.scs.rsr.protocol.http;

import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;
import com.aurawin.core.stored.Manifest;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server extends com.aurawin.core.rsr.server.Server{
    public Service Service;
    public Account Root;
    public Domain Domain;
    public Node Node;

    public Server(Manifest manifest, Service service) throws IOException, NoSuchMethodException,
            InstantiationException,IllegalAccessException
    {
        super(new InetSocketAddress(service.getIP(),service.getPort()), HTTP_1_1.class, false, service.getHostname());

        Service = service;
        Node = service.getNode();
        Domain = Node.getDomain();
        Root = Entities.Lookup(Account.class, Domain.getRootId());
    }
}
