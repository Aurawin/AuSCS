package com.aurawin.scs.rsr.protocol.audisk.def.version;


import com.aurawin.core.array.VarString;
import com.aurawin.scs.lang.Table;
import com.aurawin.scs.solution.Settings;


import java.util.regex.Pattern;

public class Version extends com.aurawin.core.rsr.def.Version {
    public Version(){
        super (1,0, Settings.AuDisk.Protocol,"%s:%d.%d");
    }
    public Version(int major, int minor) {
        super(major, minor, Settings.AuDisk.Protocol,"%s:%d.%d");
    }

    @Override
    public void Reset(){
        super.Reset();
    }
    @Override
    public void Release(){
        super.Release();
    }
    @Override
    public boolean Load(String input) {
        String[] aData = input.split(":");
        if (aData.length==2){
            Protocol=aData[0];
            String[] aVersion=aData[1].split(Pattern.quote("."));
            if (aVersion.length==2){
                Major= VarString.toInteger(aVersion[0],1);
                Minor= VarString.toInteger(aVersion[1],0);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

}
