package com.aurawin.scs.rsr.protocol.audisk.def;

import com.aurawin.core.stream.MemoryStream;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request {
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
    public static volatile long Id = 1;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("S")
    public long Size;
    public transient MemoryStream Payload;

    public Request() {
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
        Protocol = null;
        Command = null;
        Method = null;
        Payload.Release();
        Payload=null;
    }
}
