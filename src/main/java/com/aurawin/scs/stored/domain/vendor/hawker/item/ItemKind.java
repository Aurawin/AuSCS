package com.aurawin.scs.stored.domain.vendor.hawker.item;


public enum ItemKind {
    Singleton ("Singleton"),
    Collection ("Collection");
    private final String value;
    ItemKind(String val) {
        value = val;
    }
    public String getValue(){return value;}

    public static ItemKind fromString(String value){
        for (ItemKind k : ItemKind.values()){
            if (k.getValue().compareToIgnoreCase(value)==0){
                return k;
            }
        }
        return null;
    }
}
