package com.aurawin.scs.rsr.protocol.audisk.def;

import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.google.gson.annotations.Expose;
import com.aurawin.core.rsr.transport.methods.Method;
import com.google.gson.annotations.SerializedName;

import javax.persistence.Transient;

public class Request {
    public transient AUDISK Owner;

    @Expose(serialize = false, deserialize = false)
    private static volatile long MasterId = 0;

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
        MasterId +=1;
        Id = MasterId;
    }

    public void Reset(){
        Protocol = "";
        Method = "";
        Command = "";

        MasterId +=1;
        Id = MasterId;

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
        Owner = src.Owner;
        Protocol = src.Protocol;
        Command = src.Command;
        Method = src.Method;
        Payload = src.Payload;
        src.Payload = new MemoryStream();
    }

}
