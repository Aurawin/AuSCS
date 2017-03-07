package com.aurawin.scs.rsr.protocol.audisk.server;

import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.stored.cloud.Disk;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.core.stored.Manifest;
import com.aurawin.scs.stored.cloud.Service;

import java.io.IOException;
import java.util.ArrayList;

public class Server extends com.aurawin.core.rsr.server.Server {
    public Service Service;

    protected ArrayList<Stored> Disks;
    protected ArrayList<Stored> AllDisks;
    public Server(Manifest manifest, Service service) throws IOException, NoSuchMethodException,
            Exception, InstantiationException,IllegalAccessException
    {
        super(AUDISK.class, false);

        Service = service;
        Disks = Entities.Lookup(Disk.class.getAnnotation(QueryByOwnerId.class),service.getNode().getId());
    }

    public Disk getDisk(long id){
        return (Disk) Disks.stream()
                .filter(Disk.class ::isInstance)
                .filter(d -> d.getId()==id)
                .findFirst()
                .orElse(null);
    }

}
