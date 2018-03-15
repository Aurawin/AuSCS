package com.aurawin.scs.rsr.protocol.audisk.def;

import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.rsr.protocol.transport.AUDISK;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.aurawin.core.rsr.transport.methods.Result;

import static com.aurawin.core.rsr.transport.methods.Result.None;

public class Response {
    public transient AUDISK Owner;
    public transient MemoryStream Payload;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("P")
    public String Protocol;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("M")
    public String Method;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("S")
    public long Size;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("I")
    public long Id;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("C")
    public Result Code;



    public Response(AUDISK owner) {
        Owner = owner;
        Payload = new MemoryStream();
        Reset();
    }

    public void Reset(){
        Protocol = "";
        Method = "";
        Size = 0;
        Id = 0;
        Code = None;
        Payload.Clear();
    }

    public void Release(){
        Owner = null;
        Protocol = null;
        Method = null;
        Code  = null;
        Payload.Release();
        Payload=null;
    }
    public void Assign(Response src){
        Owner = src.Owner;
        Protocol = src.Protocol;
        Method = src.Method;
        Code = src.Code;
        Payload=src.Payload;
        Size = src.Size;
        Id = src.Id;
        src.Payload= new MemoryStream();
    }

}
