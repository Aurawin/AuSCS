package com.aurawin.scs.rsr.protocol.audisk.method;


import com.aurawin.core.array.Bytes;
import com.aurawin.core.json.Builder;

import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.rsrResult;
import com.aurawin.core.solution.Settings;
import com.aurawin.core.stream.MemoryStream;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.aurawin.scs.rsr.protocol.audisk.def.Request;
import static com.aurawin.core.rsr.def.rsrResult.rFailure;
import static com.aurawin.core.rsr.def.rsrResult.rPostpone;
import static com.aurawin.core.rsr.def.rsrResult.rSuccess;

public abstract class Method extends com.aurawin.core.rsr.transport.methods.Item {
    protected com.aurawin.core.rsr.Item Owner;

    public Method(Item owner, String Name) {
        super(Name);
        Owner = owner;
    }


}
