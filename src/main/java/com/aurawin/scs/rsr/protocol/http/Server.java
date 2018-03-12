package com.aurawin.scs.rsr.protocol.http;

import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;
import com.aurawin.core.stored.Manifest;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;

import org.hibernate.Session;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

import static com.aurawin.scs.stored.bootstrap.Plugins.initializePlugin;

public class Server extends com.aurawin.core.rsr.server.Server{
    public Service Service;
    public Account Root;
    public Domain Domain;
    public Node Node;


    public static void Bootstrap(){
        Session ssn = Entities.openSession();
        try{
            initializePlugin(com.aurawin.scs.core.admin.cms.CMS.class,ssn);
            initializePlugin(com.aurawin.scs.core.admin.cms.Domain.class,ssn);
            initializePlugin(com.aurawin.scs.core.admin.cms.Template.class,ssn);
            initializePlugin(com.aurawin.scs.core.admin.ACL.class,ssn);
            initializePlugin(com.aurawin.scs.core.admin.Admin.class,ssn);
            initializePlugin(com.aurawin.scs.core.admin.Cloud.class,ssn);

            initializePlugin(com.aurawin.scs.core.Account.class,ssn);
            initializePlugin(com.aurawin.scs.core.ACL.class,ssn);
            initializePlugin(com.aurawin.scs.core.Avatar.class,ssn);
            initializePlugin(com.aurawin.scs.core.Login.class,ssn);
            initializePlugin(com.aurawin.scs.core.Noid.class,ssn);

        } finally{
            ssn.close();
        }
        com.aurawin.scs.stored.bootstrap.roles.Administrator.Initialize();
        com.aurawin.scs.stored.bootstrap.roles.PowerUser.Initialize();
        com.aurawin.scs.stored.bootstrap.roles.User.Initialize();
    }

    public Server(Manifest manifest, Service service) throws NoSuchMethodException,InvocationTargetException,IOException, NoSuchMethodException,
            InstantiationException,IllegalAccessException
    {
        super(new InetSocketAddress(service.getIP(),service.getPort()), HTTP_1_1.class, false, service.getHostname());

        Service = service;
        Node = service.getNode();
        Domain = Node.getDomain();
        Root = Entities.Lookup(Account.class, Domain.getRootId());
    }
}
