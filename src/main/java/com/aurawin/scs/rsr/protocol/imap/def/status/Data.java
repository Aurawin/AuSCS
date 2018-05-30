package com.aurawin.scs.rsr.protocol.imap.def.status;

public enum Data {
    Messages("MESSAGES"),
    Recent("RECENT"),
    UIDNext("UIDNEXT"),
    UIDValidity("UIDVALIDITY"),
    Unseen("UNSEEN"),
    Deleted("EXPUNGE"),
    Exists("EXISTS");

    String Value;

    public String value(){
        return Value;
    }

    Data(String value){
        Value=value;
    }


}
