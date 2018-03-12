package com.aurawin.scs.rsr.protocol.audisk.method.command;


import com.aurawin.core.log.Syslog;
import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.methods.Item;
import com.aurawin.core.rsr.transport.methods.Result;
import com.aurawin.scs.lang.Table;
import com.aurawin.scs.rsr.protocol.audisk.client.Client;
import com.aurawin.scs.rsr.protocol.audisk.def.Request;
import com.aurawin.scs.rsr.protocol.audisk.def.Status;
import com.aurawin.scs.rsr.protocol.audisk.server.Server;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.cloud.Disk;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.hibernate.Session;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.aurawin.core.rsr.transport.methods.Result.Failure;
import static com.aurawin.core.rsr.transport.methods.Result.None;
import static com.aurawin.core.rsr.transport.methods.Result.Ok;

public class cDeleteFolder extends Item {
    @Expose(serialize = true, deserialize = true)
    @SerializedName("DSK")
    public long DiskId;
    @Expose(serialize = true, deserialize = true)
    @SerializedName("NID")
    public long NamespaceId;
    @Expose(serialize = true, deserialize = true)
    @SerializedName("DID")
    public long DomainId;
    @Expose(serialize = true, deserialize = true)
    @SerializedName("OID")
    public long OwnerId;
    @Expose(serialize = true, deserialize = true)
    @SerializedName("FID")
    public long FolderId;

    public cDeleteFolder() {
        super(Settings.AuDisk.Method.Folder+"."+Settings.AuDisk.Method.Command.Delete);
    }

    @Override
    public Result onProcess(Session ssn, Transport transport){
        Result r = None;
        cDeleteFolder cmd = null;
        AUDISK t = (AUDISK) transport;

        switch (t.Kind){
            case Server:
                Server s = (Server) t.Owner.Engine;
                cmd = t.gson.fromJson(t.Request.Command,cDeleteFolder.class);
                Disk disk = s.getDisk(cmd.DiskId);
                if (disk!=null) {
                    Path dPath = Settings.Stored.Domain.Network.File.buildPath(
                            disk.getMount(),
                            cmd.NamespaceId,
                            cmd.DomainId,
                            cmd.OwnerId,
                            cmd.FolderId
                    );
                    File fPath = dPath.toFile();
                    if (fPath.isDirectory()) {
                        try {
                            Files.delete(dPath);
                            r = Ok;
                        } catch (Exception e){
                            Syslog.Append(getClass().getCanonicalName(),"Execute.Files.delete", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure,e.getMessage()));
                            r= Failure;
                        }
                    } else {
                        r = Failure;
                    }

                } else {
                    r = Failure;
                }
                break;
            case Client:
                Client c = (Client) t.Owner.Engine;
                if (t.Response.Code == Status.Ok){

                }
                Request q = t.Requests.parallelStream()
                        .filter(rq -> rq.Id==t.Response.Id)
                        .findFirst()
                        .orElse(null);
                if (q!=null) {
                    t.Requests.remove(q);
                    // todo notify completion
                    r=Ok;
                } else {
                    r = Failure;
                }
                break;
        }
        return r;
    }

}
