package com.aurawin.scs.stored.cloud.service;

import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.scs.solution.Settings;
import com.aurawin.scs.solution.Table;

import static com.aurawin.scs.solution.Table.Stored.Cloud.Service.Kind.svcAUDISK;

@Namespaced
public class AUDISK extends DefaultService {
    public AUDISK() {
        Kind =  svcAUDISK;
    }
}
