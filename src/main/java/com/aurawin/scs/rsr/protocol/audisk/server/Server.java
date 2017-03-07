package com.aurawin.scs.rsr.protocol.audisk.server;

import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.stored.cloud.Disk;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.core.stored.Manifest;
import java.io.IOException;
import java.util.ArrayList;

public class Server extends com.aurawin.core.rsr.server.Server {
    protected Node Node;
    protected ArrayList<Stored> Disks;
    protected ArrayList<Stored> AllDisks;
    public Server(Manifest manifest, long nodeId) throws IOException, NoSuchMethodException,
            Exception, InstantiationException,IllegalAccessException
    {
        super(AUDISK.class, false);

        Node = Entities.Lookup(Node.class,nodeId);
        Disks = Entities.Lookup(Disk.class.getAnnotation(QueryByOwnerId.class),nodeId);
    }

    public Disk getDisk(long id){
        return (Disk) Disks.stream()
                .filter(Disk.class ::isInstance)
                .filter(d -> d.getId()==id)
                .findFirst()
                .orElse(null);
    }

    public Node getNode(){
        return Node;
    }
}
