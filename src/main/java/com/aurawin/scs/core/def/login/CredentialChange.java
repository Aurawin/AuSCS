package com.aurawin.scs.core.def.login;


import com.aurawin.core.stream.MemoryStream;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CredentialChange {
    @Expose(serialize = true, deserialize = true)
    public long Id;
    @Expose(serialize = true, deserialize = true)
    public String PasswordOld;
    @Expose(serialize = true, deserialize = true)
    public String PasswordNew;
}
