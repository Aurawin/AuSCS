package com.aurawin.scs.core;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class CoreObjects extends HashMap<String,CoreObject> {
    public CoreResult Execute(String coNamespace, String ccNamespace){
        CoreObject co = this.get(coNamespace);
        if (co!=null) {
            CoreCommand cc = co.get(ccNamespace);
            if (cc!=null){
                co.Enter();
                try{
                    co.entered=true;
                    return cc.Execute();
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
}
