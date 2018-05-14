package com.aurawin.scs.rsr.protocol.http;

import com.aurawin.core.ClassScanner;
import com.aurawin.core.plugin.Plug;
import com.aurawin.core.plugin.annotations.Plugin;
import com.aurawin.core.rsr.IpHelper;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.core.stored.entities.UniqueId;
import com.aurawin.scs.stored.ContentType;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;
import com.aurawin.core.stored.Manifest;
import com.aurawin.scs.rsr.protocol.transport.HTTP_1_1;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.Package;

import org.hibernate.Session;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Entity;

import static com.aurawin.scs.lang.Database.Field.Cloud.Service.UniqueId;
import static com.aurawin.scs.stored.bootstrap.Plugins.initializePlugin;

public class Server extends com.aurawin.core.rsr.server.Server{
    public ArrayList<ContentType> ContentTypes = new ArrayList<>();
    public Service Service;
    public Domain Domain;
    public Node Node;


    public static void Bootstrap(){


        Session ssn = Entities.openSession();
        try {

            ClassScanner cs = new ClassScanner();
            Annotation ed = null;
            Set<Class<? extends Plug>> ca = cs.scanPackageForPlugins(Package.class);
            for (Class<? extends Plug> c : ca) {
                ed = c.getAnnotation(Plugin.class);
                if (ed != null) {
                    initializePlugin(c, ssn);
                }
                ed = c.getAnnotation(Namespaced.class);
                if (ed!=null){
                    UniqueId id = new UniqueId(c.getCanonicalName());
                    id.Identify(ssn);
                }

            }
        } catch (Exception ex){

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
        super(new InetSocketAddress(IpHelper.fromLong(service.getIP()),service.getPort()), HTTP_1_1.class, false, service.getHostname());

        Service = service;
        Node = service.getOwner();
        Domain = Node.getDomain();

    }
}
