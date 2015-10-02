package com.aurawin.core.stored.entities.vendor.hawker.manifest;


public enum ManifestKind {
    Singleton ("Singleton"),
    Collection ("Collection");
    private final String value;
    ManifestKind(String val) {
        value = val;
    }
    public String getValue(){return value;}

    public static ManifestKind fromString(String value){
        for (ManifestKind k : ManifestKind.values()){
            if (k.getValue().compareToIgnoreCase(value)==0){
                return k;
            }
        }
        return null;
    }
}
