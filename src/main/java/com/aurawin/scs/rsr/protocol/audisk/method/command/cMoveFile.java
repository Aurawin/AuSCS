package com.aurawin.scs.rsr.protocol.audisk.method.command;

import com.aurawin.core.log.Syslog;
import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.methods.Method;
import com.aurawin.core.rsr.transport.methods.Result;

import com.aurawin.scs.audisk.AuDisk;
import com.aurawin.scs.lang.Table;
import com.aurawin.scs.rsr.protocol.audisk.client.Client;
import com.aurawin.scs.rsr.protocol.audisk.def.Request;
import com.aurawin.scs.rsr.protocol.audisk.server.Server;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.stored.cloud.Disk;import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.hibernate.Session;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.aurawin.core.rsr.transport.methods.Result.Failure;
import static com.aurawin.core.rsr.transport.methods.Result.None;
import static com.aurawin.core.rsr.transport.methods.Result.Ok;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class cMoveFile extends Method {
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
    @SerializedName("OFD")
    public long OldFolderId;
    @Expose(serialize = true, deserialize = true)
    @SerializedName("NFD")
    public long NewFolderId;

    public cMoveFile() {
        super(Settings.AuDisk.Method.File+"."+ Settings.AuDisk.Method.Command.Move);
    }
    @Override
    public Result onProcess(Session ssn, Transport transport){
        Result r = None;
        AUDISK t = (AUDISK) transport;

        switch (t.Kind){
            case Server:
                Server s = (Server) t.Owner.Engine;
                cMoveFile cmd = t.gson.fromJson(t.Request.Command,cMoveFile.class);

                if (AuDisk.moveFile(cmd.DiskId,cmd.NamespaceId,cmd.DomainId,cmd.OwnerId,cmd.OldFolderId,cmd.NewFolderId,cmd.FileId)){
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
