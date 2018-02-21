package com.aurawin.scs.rsr.protocol.audisk.def;

import com.aurawin.core.stream.MemoryStream;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.aurawin.scs.rsr.protocol.audisk.def.Status.None;

public class Response {
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
    public Status Code;

    public transient MemoryStream Payload;

    public Response() {
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
        Protocol = null;
        Method = null;
        Code  = null;
        Payload.Release();
        Payload=null;
    }
}
