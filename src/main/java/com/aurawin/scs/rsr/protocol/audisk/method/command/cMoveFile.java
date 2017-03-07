package com.aurawin.scs.rsr.protocol.audisk.method.command;

import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.rsr.protocol.audisk.method.command.Command;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class cMoveFile extends Command {
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
        super(com.aurawin.scs.lang.Table.AuDisk.Method.Command.MoveFile);
    }
}
