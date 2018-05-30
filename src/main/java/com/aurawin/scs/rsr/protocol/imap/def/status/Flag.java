package com.aurawin.scs.rsr.protocol.imap.def.status;

public enum Flag {
    Answered("\\Answered"),
    Flagged("\\Flagged"),
    Deleted("\\Deleted"),
    Seen("\\Seen"),
    Any("\\*"),
    Draft("\\Draft"),
    NoSelect("\\Noselect");

    String Value;
    public String value(){
        return Value;
    }
    Flag(String value){ Value=value;}
}
