package com.aurawin.scs.rsr.protocol.audisk.def;

import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.google.gson.annotations.Expose;
import com.aurawin.core.rsr.transport.methods.Method;
import com.google.gson.annotations.SerializedName;

import javax.persistence.Transient;

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

    public Request(AUDISK owner) {
        Owner = owner;
        Payload = new MemoryStream();
        Reset();
        Id = Owner.Id.getId();
    }

    public void Reset(){
        Protocol = "";
        Method = "";
        Command = "";

        Id = Owner.Id.Spin();

        Size = 0;
        Payload.Clear();

    }

    public void Release(){
        Owner=null;
        Protocol = null;
        Command = null;
        Method = null;
        Payload.Release();
        Payload=null;
    }

    public void Assign(Request src){
        Reset();

        Owner = src.Owner;
        Protocol = src.Protocol;
        Command = src.Command;
        Method = src.Method;
        Payload.CopyFrom(src.Payload);
    }

}
