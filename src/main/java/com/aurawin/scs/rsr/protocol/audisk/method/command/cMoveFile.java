package com.aurawin.scs.rsr.protocol.audisk.method.command;

import com.aurawin.core.log.Syslog;
import com.aurawin.core.rsr.transport.Transport;
import com.aurawin.core.rsr.transport.methods.Item;
import com.aurawin.core.rsr.transport.methods.Result;
import com.aurawin.scs.lang.Table;
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
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class cMoveFile extends Item {
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
        super(Table.AuDisk.Method.File+"."+ Table.AuDisk.Method.Command.Move);
    }
    @Override
    public Result onProcess(Session ssn, Transport transport){
        Result r = None;
        AUDISK t = (AUDISK) transport;

        switch (t.Kind){
            case Server:
                Server s = (Server) t.Owner.Engine;
                Disk disk = s.getDisk(DiskId);
                if (disk!=null) {
                    Path Mount = Settings.Stored.Domain.Network.File.buildMount(disk.getMount());
                    Path OldPath = Settings.Stored.Domain.Network.File.buildPath(
                            disk.getMount(),
                            NamespaceId,
                            DomainId,
                            OwnerId,
                            OldFolderId
                    );
                    Path NewPath = Settings.Stored.Domain.Network.File.buildPath(
                            disk.getMount(),
                            NamespaceId,
                            DomainId,
                            OwnerId,
                            NewFolderId
                    );
                    Path OldFile = Settings.Stored.Domain.Network.File.buildFilename(
                            disk.getMount(),
                            NamespaceId,
                            DomainId,
                            OwnerId,
                            OldFolderId,
                            FileId
                    );
                    Path NewFile = Settings.Stored.Domain.Network.File.buildFilename(
                            disk.getMount(),
                            NamespaceId,
                            DomainId,
                            OwnerId,
                            NewFolderId,
                            FileId
                    );
                    File dMount = Mount.toFile();
                    File dOldPath = OldPath.toFile();
                    File dNewPath = NewPath.toFile();
                    if (dMount.isDirectory()==true) {
                        if (dOldPath.isDirectory()==true) {
                            if (!dNewPath.isDirectory()) {
                                try {
                                    Files.createDirectories(NewPath, Settings.Stored.Cloud.Disk.Attributes);
                                } catch (Exception e){
                                    Syslog.Append(getClass().getCanonicalName(),"Execute.createDirectories", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure,e.getMessage()));
                                    r = Failure;
                                }
                            }
                            try {
                                Files.move(OldFile, NewFile, REPLACE_EXISTING);
                                r = Ok;
                            } catch (Exception e){
                                Syslog.Append(getClass().getCanonicalName(),"Execute.Files.move", com.aurawin.core.lang.Table.Format(com.aurawin.core.lang.Table.Error.RSR.MethodFailure,e.getMessage()));
                                r = Failure;
                            }
                        } else {
                            r = Failure;
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
