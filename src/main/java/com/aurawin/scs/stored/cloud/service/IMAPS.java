package com.aurawin.scs.stored.cloud.service;


import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.scs.solution.Settings;

import static com.aurawin.scs.solution.Table.Stored.Cloud.Service.Kind.svcIMAPS;

@Namespaced
public class IMAPS extends DefaultService{
    public IMAPS() {
        Kind = svcIMAPS;
    }
}