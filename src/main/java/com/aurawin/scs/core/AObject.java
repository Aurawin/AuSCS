package com.aurawin.scs.core;

import com.aurawin.core.stored.entities.UniqueId;
import org.hibernate.Session;

public abstract class AObject {

    protected boolean initialized = false;
    protected boolean entered=false;

    protected UniqueId Id;

    public Verify(Session ssn){
        Id.Verify(ssn);
    }

    public AObject(String namespace) {
        Id = new UniqueId(namespace);
    }

    protected abstract CoreResult Initialize();
    protected abstract CoreResult Finalize();
    protected abstract CoreResult Enter();
    protected abstract CoreResult Exit();
}
