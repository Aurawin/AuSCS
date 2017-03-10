package com.aurawin.scs.core.def.admin;

import com.aurawin.scs.stored.domain.Domain;
import com.google.gson.annotations.Expose;

public class Certificate {

    @Expose(serialize = true, deserialize = true)
    public long Id;

    @Expose(serialize = true, deserialize = true)
    public long LocationId;

    @Expose(serialize = true, deserialize = true)
    public long DomainId;

    @Expose(serialize = true, deserialize = true)
    public String Email;

    @Expose(serialize = true, deserialize = true)
    public int Days;
}
