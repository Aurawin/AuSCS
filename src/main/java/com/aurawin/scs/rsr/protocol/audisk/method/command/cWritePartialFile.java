package com.aurawin.scs.rsr.protocol.audisk.method.command;

import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.methods.Method;
import com.aurawin.core.rsr.transport.methods.Result;
import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.rsr.protocol.audisk.server.Server;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Settings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.hibernate.Session;

import static com.aurawin.core.rsr.transport.methods.Result.*;

public class cWritePartialFile extends Method{
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
    public long FileId;
    @Expose(serialize = true, deserialize = true)
    @SerializedName("FLD")
    public long FolderId;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("SRT")
    public long Start;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("LNH")
    public int Length;

    public cWritePartialFile() {
        super(Settings.AuDisk.Method.File+"."+Settings.AuDisk.Method.Command.PartialWrite);
    }

    @Override
    public Result onProcess(Session ssn, Transport transport){
        Result r = None;
        AUDISK t = (AUDISK) transport;
        switch (t.Kind) {
            case Server:
                Server s = (Server) t.Owner.Engine;
                cWritePartialFile cmd = t.gson.fromJson(t.Request.Command, cWritePartialFile.class);
                if (AuDisk.writePartialFile(t.Request.Payload,cmd.DiskId,cmd.NamespaceId,cmd.DomainId,cmd.OwnerId,cmd.FolderId,cmd.FileId,cmd.Start,cmd.Length)){
                    r = Ok;
                } else {
                    r = Failure;
                }

                break;
            case Client:
                    r = Ok;
                break;

        }

        return r;
    }
}
