package com.aurawin.scs.rsr.protocol.imap.def.status;

/**
 * Created by atbrunner on 2/12/18.
 */
public enum Status {
    sNone("None"),
    sOK ("%s OK"),
    sFail("%s Failure");

    Status(String value){
        this.value = value;
    }
    private final String value;

    public String getValue(){return value;}
}
