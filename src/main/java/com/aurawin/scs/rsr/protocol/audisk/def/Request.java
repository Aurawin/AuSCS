package com.aurawin.scs.rsr.protocol.audisk.def;

import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.google.gson.annotations.Expose;
import com.aurawin.core.rsr.transport.methods.Method;
import com.google.gson.annotations.SerializedName;

import javax.persistence.Transient;
import java.time.Instant;

public class Request {
    public transient AUDISK Owner;


    @Expose(serialize = true, deserialize = true)
    @SerializedName("P")
    public String Protocol;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("M")
    public String Method;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("C")
    public String Command;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("I")
    public long Id = 1;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("S")
    public long Size;

    public transient MemoryStream Payload;
    public transient Instant TTL;

    public Request(AUDISK owner) {
        Owner = owner;
        Payload = new MemoryStream();
    }

    public void Reset(){
        Id=0;
        Protocol = "";
        Method = "";
        Command = "";

        Size = 0;
        if (Payload!=null)
          Payload.Clear();
    }

    public void Release(){
        if (Owner!=null)
            Owner.Requests.remove(this);
        Owner=null;
        Protocol = null;
        Command = null;
        Method = null;
        if (Payload!=null)
          Payload.Release();
        Payload=null;
    }

    public void Assign(Request src){
        Id = src.Id;
        Protocol = src.Protocol;
        Command = src.Command;
        Method = src.Method;
        Size = src.Size;
        Payload.CopyFrom(src.Payload);
    }

}
