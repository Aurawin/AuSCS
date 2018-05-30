package com.aurawin.scs.rsr.protocol.imap.def.status;

public enum Response
{  Ok("OK"), No("NO"), Bad ("BAD"),Byte("BYE"),Any("*");

    String Value;

    public String value(){
        return Value;
    }

    Response(String value){
        Value = value;
    }
}
