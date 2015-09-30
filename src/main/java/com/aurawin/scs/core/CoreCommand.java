package com.aurawin.scs.core;

import com.aurawin.core.stored.entities.UniqueId;
import org.hibernate.Session;

public abstract class CoreCommand {
    protected UniqueId Id;

    public CoreCommand(String namespace){
        Id = new UniqueId(namespace);
    }

    public CoreResult Verify(Session ssn){
        Id.Verify(ssn);

        return CoreResult.Ok;
    }
    protected abstract CoreResult Initialize();
    protected abstract CoreResult Finalize();
    protected abstract CoreResult Execute(CoreContext Context);
}
