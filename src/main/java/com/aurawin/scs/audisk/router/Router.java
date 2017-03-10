package com.aurawin.scs.audisk.router;

import com.aurawin.core.json.Builder;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.lang.Table;
import com.aurawin.scs.rsr.protocol.audisk.client.Client;
import com.aurawin.scs.rsr.protocol.audisk.def.Request;
import com.aurawin.scs.rsr.protocol.audisk.def.version.Version;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cMakeFolder;

import com.aurawin.scs.stored.cloud.Disk;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;
import com.google.gson.Gson;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Router {
    public static Node Node;
    public static Version Version = new Version();
    public static ConcurrentHashMap<Long,Route> Routes = new ConcurrentHashMap<Long, Route>();

    public static void Initialize(Node node){
        Node = node;
    }
    public static void scanForRoutes(){
        invalidateRoutes();
        ArrayList<Stored> Disks = Entities.Lookup(Disk.class.getAnnotation(QueryAll.class));
        for (Stored s: Disks){
            Disk d = (Disk) s;
            if (d.getOwnerId()!=Node.getId()){
                Route r = Routes.get(d.getId());
                if (r==null) {
                    r = new Route();
                    r.Disks=Entities.Lookup(Disk.class.getAnnotation(QueryByOwnerId.class),d.getOwnerId());
                    r.Node=Entities.Lookup(Node.class,d.getOwnerId());
                    r.Service=Entities.Lookup(Service.class,d.getServiceId());
                    r.Valid=true;
                    InetSocketAddress bind=new InetSocketAddress(r.Service.getIP(),r.Service.getPort());
                    InetSocketAddress remote=new InetSocketAddress(r.Service.getIP(),r.Service.getPort());
                    try {
                        r.Connector = new Client(bind, remote);
                    } catch (Exception ex){

                    }
                    Routes.put(d.getId(), r);
                }
            }

        }
    }
    public static void invalidateRoutes(){
        Routes.values().stream().forEach(r -> r.Valid = false);
    }
    public static ArrayList<String> listFiles(long DiskId, long NamespaceId, long DomainId, long OwnerId, long FolderId){
        // construct query...
        // send query
        // get result...
        // parse results...

        return new ArrayList<String>();
    }
    public static void makeDirectory(long DiskId, long NamespaceId, long DomainId, long OwnerId, long FolderId){
        // construct query...
        // send query
        // get result...
        Builder bldr = new Builder();
        Gson gson = bldr.Create();

        cMakeFolder cmd = new cMakeFolder();
        cmd.DiskId=DiskId;
        cmd.NamespaceId=NamespaceId;
        cmd.DomainId=DomainId;
        cmd.OwnerId=OwnerId;
        cmd.FolderId=FolderId;

        Request rq = new Request();
        rq.Command=gson.toJson(cmd);
        rq.Method=Table.AuDisk.Method.MakeFolder;
        rq.Protocol=Version.toString();


    }
}
