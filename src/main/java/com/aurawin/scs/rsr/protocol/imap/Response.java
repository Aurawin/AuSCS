package com.aurawin.scs.rsr.protocol.imap;

import com.aurawin.core.array.KeyPairs;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.core.rsr.def.Version;
import com.aurawin.scs.rsr.protocol.imap.def.ResolveResult;
import com.aurawin.scs.rsr.protocol.imap.def.Version_4_1;
import com.aurawin.scs.rsr.protocol.imap.def.status.Status;
import static com.aurawin.scs.rsr.protocol.imap.def.status.Status.sNone;

public class Response {
    public KeyPairs Parameters;
    public Status Status;
    public String Sequence;
    public ResolveResult Result;
    public Version Version;
    public MemoryStream Payload;

    public Response() {
        Parameters = new KeyPairs();
        Parameters.DelimiterItem="&";
        Parameters.DelimiterField="=";
        Status = sNone;
        Version = new Version_4_1();
    }
    public void Reset(){
        Parameters.Empty();
        Payload.Clear();
        Version.Reset();
        Status = null;
    }

    public void Release(){
        Parameters.Release();
        Payload.Release();
        Version.Release();
    }
}
