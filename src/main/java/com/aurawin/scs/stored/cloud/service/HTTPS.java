package com.aurawin.scs.stored.cloud.service;


import com.aurawin.core.stored.annotations.Namespaced;
import com.aurawin.scs.solution.Settings;

import static com.aurawin.scs.solution.Table.Stored.Cloud.Service.Kind.svcHTTPS;

@Namespaced
public class HTTPS extends DefaultService {
    public HTTPS() {
        Kind = svcHTTPS;
    }
}
