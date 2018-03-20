package com.aurawin.scs.rsr.protocol.audisk.method.command;

import com.aurawin.core.log.Syslog;
import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.methods.Method;
import com.aurawin.core.rsr.transport.methods.Result;
import com.aurawin.scs.rsr.protocol.audisk.client.Client;
import com.aurawin.scs.rsr.protocol.audisk.def.Request;
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

public class cMakeFolder extends Method {
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


    public cMakeFolder() {
        super(Settings.AuDisk.Method.Folder+"."+Settings.AuDisk.Method.Command.Make);
    }

    @Override
    public Result onProcess(Session ssn, Transport transport){
        cMakeFolder cmd= null;
        Result r = None;
        AUDISK t = (AUDISK) transport;

        switch (t.Kind){
            case Server:
                Server s = (Server) t.Owner.Engine;
                cmd = t.gson.fromJson(t.Request.Command,cMakeFolder.class);
                Disk disk = s.getDisk(cmd.DiskId);
                if (disk!=null) {
                    if (disk.makeFolder(
                            cmd.NamespaceId,
                            cmd.DomainId,
                            cmd.OwnerId,
                            cmd.FolderId
                    )) {
                        r = Ok;
                    } else{
                        r=Failure;
                    }
                } else {
                    r=Failure;
                }

                break;
            case Client:
                    r=Ok;
                break;
        }
        return r;
    }
}
