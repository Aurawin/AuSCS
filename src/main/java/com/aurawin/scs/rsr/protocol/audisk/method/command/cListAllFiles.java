package com.aurawin.scs.rsr.protocol.audisk.method.command;

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
import java.nio.file.Path;

import static com.aurawin.core.rsr.transport.methods.Result.Failure;
import static com.aurawin.core.rsr.transport.methods.Result.None;
import static com.aurawin.core.rsr.transport.methods.Result.Ok;

public class cListAllFiles extends Item {

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


    public cListAllFiles() {
        super(Table.AuDisk.Method.File+"."+Table.AuDisk.Method.Command.ListAll);
    }

    @Override
    public Result onProcess(Session ssn, Transport transport){
        Result r = None;
        cListAllFiles cmd = null;
        AUDISK t = (AUDISK) transport;

        switch (t.Kind) {
            case Server:
                Server s = (Server) t.Owner.Engine;
                cmd = t.gson.fromJson(t.Request.Command,cListAllFiles.class);
                Disk disk = s.getDisk(cmd.DiskId);
                if (disk!=null) {
                    Path scanPath = Settings.Stored.Domain.Network.File.buildPath(
                            disk.getMount(),
                            cmd.NamespaceId,
                            cmd.DomainId,
                            cmd.OwnerId,
                            cmd.FolderId
                    );
                    File dScanPath = scanPath.toFile();
                    File[] dList = dScanPath.listFiles();

                    for (File d:dList){
                        t.Response.Payload.Write(d.getName());
                        t.Response.Payload.Write(com.aurawin.core.lang.Table.CRLF);
                    }

                    r = Ok;

                } else {
                    r = Failure;
                }

                break;

            case Client:
                Client c = (Client) t.Owner.Engine;

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
