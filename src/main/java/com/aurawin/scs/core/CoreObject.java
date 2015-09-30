package com.aurawin.scs.core;

import com.aurawin.core.stored.entities.UniqueId;
import org.hibernate.Session;

import java.util.HashMap;

public abstract class CoreObject extends HashMap<String,CoreCommand> {
    protected boolean initialized = false;
    protected boolean entered=false;

    protected UniqueId Id;

    public CoreObject(String namespace) {
        Id = new UniqueId(namespace);
    }

    public CoreResult Verify(Session ssn){
        Id.Verify(ssn);
        return CoreResult.Ok;
    }


    protected abstract CoreResult Initialize();
    protected abstract CoreResult Finalize();
    protected abstract CoreResult Enter();
    protected abstract CoreResult Exit();
}
