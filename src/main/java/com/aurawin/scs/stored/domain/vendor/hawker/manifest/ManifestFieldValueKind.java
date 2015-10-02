package com.aurawin.core.stored.entities.vendor.hawker.manifest;

public enum ManifestFieldValueKind {
    String ("String",255l),
    Memo ("Memo",1024*1014*1024l),
    StringList("StringList",1024*1024*2l),
    Integer ("Integer",0l),
    Int64 ("Int64",0l),
    KeyPair("KeyPair",1024*1024*10l),
    KeyPairList ("KeyPairList",1024*1024*1024l),
    Double ("Double",0l),
    Boolean ("Boolean",0l);
    private final String value;
    private final long length;
    ManifestFieldValueKind(String val, long len) {
        value = val;
        length = len;
    }
    public String getValue(){return value;}
    public long getLength(){return length;}

    public static ManifestFieldValueKind fromString(String value){
        for (ManifestFieldValueKind k : ManifestFieldValueKind.values()){
            if (k.getValue().compareToIgnoreCase(value)==0){
                return k;
            }
        }
        return null;
    }




}
