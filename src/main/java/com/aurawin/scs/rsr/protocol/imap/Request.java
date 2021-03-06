package com.aurawin.scs.rsr.protocol.imap;

import com.aurawin.core.array.KeyPairs;
import com.aurawin.core.rsr.Item;
import com.aurawin.core.rsr.def.Version;

import com.aurawin.core.rsr.def.rsrResult;
import com.aurawin.core.stream.MemoryStream;
import com.aurawin.scs.rsr.protocol.imap.def.QueryResolver;
import com.aurawin.scs.rsr.protocol.imap.def.ResolveResult;
import com.aurawin.scs.rsr.protocol.imap.def.Version_4_1;
import org.hibernate.Session;

import static com.aurawin.scs.rsr.protocol.imap.def.ResolveResult.rrNone;
import static com.aurawin.core.rsr.def.rsrResult.rPostpone;


public class Request implements QueryResolver {
    protected Item Owner;
    public Version Version;
    public com.aurawin.core.rsr.transport.methods.Method Method;
    public KeyPairs Parameters;
    public MemoryStream Payload;

    public Request(Item owner) {
        Method = null;
        Owner=owner;
        Version = new Version_4_1();
    }
    @Override
    public void Release(){
        Version.Release();
        Parameters.Release();
        Payload.Release();
    }
    @Override
    public void Reset(){

    }
    @Override
    public rsrResult Peek() {
        return rPostpone;
    }

    @Override
    public rsrResult Read(){
        return rsrResult.rPostpone;
    }
    @Override
    public rsrResult Read(byte[] input){
        return rsrResult.rPostpone;
    }
    @Override
    public ResolveResult Resolve(Session ssn) {
        ResolveResult Result = rrNone;
        return Result;
    }
}
