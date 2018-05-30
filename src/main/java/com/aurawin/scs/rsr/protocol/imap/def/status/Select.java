package com.aurawin.scs.rsr.protocol.imap.def.status;

public enum Select {
    Required("REQUIRED"),
    Flags("FLAGS"),
    Exists("EXISTS"),
    Recent("RECENT"),
    Unseen("UNSEEN"),
    PermanentFlags("PERMANENTFLAGS"),
    UIDNext("UIDNEXT"),
    UIDValidity("UIDVALIDITY");

    String Value;
    public String value(){
        return Value;
    }
    Select(String value){ Value=value;}
}
