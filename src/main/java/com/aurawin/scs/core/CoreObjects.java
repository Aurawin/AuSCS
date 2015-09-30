package com.aurawin.scs.core;

import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashMap;


public class CoreObjects extends HashMap<String,CoreObject> {
    public CoreResult Execute(String coNamespace, String ccNamespace, CoreContext Context){
        CoreObject co = this.get(coNamespace);
        if (co!=null) {
            CoreCommand cc = co.get(ccNamespace);
            if (cc!=null){
                co.Enter();
                try{
                    co.entered=true;
                    return cc.Execute(Context);
                } finally{
                    co.Exit();
                    co.entered=false;
                }
            } else {
                return CoreResult.CoreCommandNotFound;
            }
        } else {
            return CoreResult.CoreObjectNotFound;
        }
    }

    public CoreResult Verify(Session ssn){
        CoreResult cr;
        for (CoreObject co : values()) {
            cr = co.Verify(ssn);
            if (cr != CoreResult.Ok) return cr;
        }
        return CoreResult.Ok;
    }

}
