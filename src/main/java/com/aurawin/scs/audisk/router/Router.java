package com.aurawin.scs.audisk.router;

import com.aurawin.core.log.Syslog;
import com.aurawin.core.rsr.IpHelper;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.EngineState;
import com.aurawin.core.rsr.def.TransportConnect;
import com.aurawin.core.stored.Stored;
import com.aurawin.core.stored.annotations.QueryAll;
import com.aurawin.core.stored.annotations.QueryByOwnerId;
import com.aurawin.core.stored.entities.Entities;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.rsr.protocol.audisk.client.Client;
import com.aurawin.scs.rsr.protocol.audisk.def.Request;
import com.aurawin.scs.rsr.protocol.audisk.def.Response;
import com.aurawin.scs.rsr.protocol.audisk.def.version.Version;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cListFiles;
import com.aurawin.scs.rsr.protocol.audisk.method.command.cMakeFolder;

import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Namespace;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.cloud.Disk;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.aurawin.core.rsr.transport.methods.Result.Ok;

public class Router {
    private static boolean TimerStarted=false;
    protected static RouterTimer Timer = new RouterTimer();
    public static Node Node;
    public static Version Version = new Version();
    public static ConcurrentHashMap<Long,Route> Routes = new ConcurrentHashMap<Long, Route>();

    public static void Initialize(Node node){
        Node = node;
        if (!TimerStarted) {
            TimerStarted=true;
            Timer.start();
        }
    }
    protected static void scanForRoutes(){
        invalidateRoutes();
        ArrayList<Stored> Disks = Entities.Lookup(Disk.class.getAnnotation(QueryAll.class));
        for (Stored s: Disks){
            Disk d = (Disk) s;
            if (d.getOwnerId()!=Node.getId()) {
                Route r = Routes.get(d.getId());
                if (r==null) {
                    Node nde = Entities.Lookup(Node.class,d.getOwnerId());
                    Service svc = Entities.Lookup(Service.class,d.getServiceId());
                    if ((nde!=null) && (svc!=null)) {
                        r = new Route(nde, svc);
                        r.Disks = Entities.Lookup(Disk.class.getAnnotation(QueryByOwnerId.class), d.getOwnerId());
                        r.Valid = true;
                        Routes.put(d.getId(), r);
                    }
                }
                if (r.Client==null){
                    InetSocketAddress bind=new InetSocketAddress(IpHelper.fromLong(Node.getIP()),r.Service.getPort());
                    InetSocketAddress remote=new InetSocketAddress(IpHelper.fromLong(r.Service.getIP()),r.Service.getPort());
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

    public static String[] listFiles(long DiskId, long NamespaceId, long DomainId, long OwnerId, long FolderId){
        Route r = Routes.get(DiskId);
        if (r!=null) {
            while (
                    (r.Client.State != EngineState.esFinalize) &&
                            (r.Connection.isAlive()==true)
                    ){
                if (r.Connection.readyForUse()) {
                    AUDISK T = (AUDISK) r.Connection.getOwnerOrWait();
                    cListFiles cmd = new cListFiles();
                    cmd.DiskId=DiskId;
                    cmd.DomainId=DomainId;
                    cmd.NamespaceId=NamespaceId;
                    cmd.FolderId=FolderId;
                    cmd.OwnerId=OwnerId;
                    int loops = 10;
                    int iLcv=1;
                    while (iLcv<=loops) {
                        Response res = T.Query(cmd, null);
                        try {
                            if (res.Code == Ok) {
                                String[] result = T.gson.fromJson(res.Payload.toString(), String[].class);
                                return result;
                            } else {
                                return null;
                            }
                        } finally {
                            res.Release();
                        }
                    }

                }
                if ( r == null ) {
                    try {
                        Thread.sleep(Settings.AuDisk.Router.ConnectionYield);
                    } catch (InterruptedException ie){
                        Syslog.Append("Router", "Thread.Sleep", ie.getMessage());
                    }
                }
            }
            return null;
        } else {
            return null;
        }
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
