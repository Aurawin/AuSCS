package com.aurawin.scs.rsr.protocol.audisk.method.command;

import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.methods.Method;
import com.aurawin.core.rsr.transport.methods.Result;
import com.aurawin.scs.rsr.protocol.audisk.server.Server;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.cloud.Disk;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.hibernate.Session;

import static com.aurawin.core.rsr.transport.methods.Result.Failure;
import static com.aurawin.core.rsr.transport.methods.Result.None;
import static com.aurawin.core.rsr.transport.methods.Result.Ok;

public class cMakeFile extends Method {
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
    @Expose(serialize = true, deserialize = true)
    @SerializedName("ID")
    public long FileId;
    public cMakeFile() {
        super(Settings.AuDisk.Method.File+"."+Settings.AuDisk.Method.Command.Make);
    }
    @Override
    public Result onProcess(Session ssn, Transport transport){
        cMakeFile cmd= null;
        Result r = None;
        AUDISK t = (AUDISK) transport;

        switch (t.Kind){
            case Server:
                Server s = (Server) t.Owner.Engine;
                cmd = t.gson.fromJson(t.Request.Command,cMakeFile.class);
                Disk disk = s.getDisk(cmd.DiskId);
                if (disk!=null) {
                    if (disk.makeFile(
                            cmd.NamespaceId,
                            cmd.DomainId,
                            cmd.OwnerId,
                            cmd.FolderId,
                            cmd.FileId
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
