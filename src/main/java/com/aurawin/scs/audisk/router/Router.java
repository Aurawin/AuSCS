package com.aurawin.scs.audisk.router;

import com.aurawin.core.log.Syslog;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.rsr.protocol.audisk.client.Client;
import com.aurawin.scs.rsr.protocol.audisk.def.Request;
import com.aurawin.scs.rsr.protocol.audisk.def.version.Version;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cListFiles;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cMakeFolder;

import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.cloud.Disk;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
                    Routes.put(d.getId(), r);
                }
                if (r.Client==null){
                    InetSocketAddress bind=new InetSocketAddress(Node.getIP(),r.Service.getPort());
                    InetSocketAddress remote=new InetSocketAddress(r.Service.getIP(),r.Service.getPort());
                    try {
                        r.Client = new Client(bind, remote);
                        r.Connection = r.Client.Connect(remote, Settings.RSR.TransportConnect.Persist.Infinite);
                    } catch (Exception ex){
                        Syslog.Append("Router", "scanForRoutes.MakeClientConnection",ex.getMessage());
                    }
                }
            }
        }
        // Release disks from pool of disks on route
        List<Route> stale =Routes.values().stream().filter(r->r.Valid==false).collect(Collectors.toList());
        for (Route r:stale) {
            for (Stored d : r.Disks){
                r.Client.Close();
                Routes.remove(d.getId());
            }
        }
    }
    public static void invalidateRoutes(){
        Routes.values().stream().forEach(r -> r.Valid = false);
    }
    public static ArrayList<String> listAllFiles(long DiskId, long NamespaceId, long DomainId, long OwnerId, long FolderId){
        Route r = Routes.get(DiskId);
        AUDISK t = (AUDISK) r.Connection.getOwnerOrWait();

        cListFiles cmd =new cListFiles();
        cmd.DiskId=DiskId;
        cmd.NamespaceId=NamespaceId;
        cmd.DomainId=DomainId;
        cmd.OwnerId=OwnerId;
        cmd.FolderId=FolderId;

        AUDISK rsr = (AUDISK) r.Connection.getOwnerOrWait();
        Request req = new Request(rsr);

        return new ArrayList<String>();
    }
    public static void makeDirectory(long DiskId, long NamespaceId, long DomainId, long OwnerId, long FolderId){
        // construct query...
        // send query
        // get result...
        Route r = Routes.get(DiskId);
        AUDISK t = (AUDISK) r.Connection.getOwnerOrWait();


        cMakeFolder cmd = new cMakeFolder();
        cmd.DiskId=DiskId;
        cmd.NamespaceId=NamespaceId;
        cmd.DomainId=DomainId;
        cmd.OwnerId=OwnerId;
        cmd.FolderId=FolderId;

        Request rq = new Request(t);



    }
}
