package com.aurawin.scs.rsr.protocol.audisk.method.command;

import com.aurawin.scs.lang.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class cMakeFolder extends Command {
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
        super(Table.AuDisk.Method.Command.MakeFolder);
    }
}
