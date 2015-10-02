package com.aurawin.core.stored.entities.vendor.hawker.manifest;

import com.aurawin.core.Memo;
import com.aurawin.core.array.KeyItem;
import com.aurawin.core.array.KeyPair;
import com.aurawin.core.array.VarString;
import com.aurawin.core.lang.Table;

public class ManifestFieldValue {

    private final ManifestFieldValueKind kind;
    private Object value;
    public ManifestFieldValueKind getKind(){ return kind;}
    public ManifestFieldValue(ManifestFieldValueKind aKind){
        kind = aKind;
    }
    public Object getData(){return value;}
    public void setValue(Object val){
        value = val;
    }
    public void setValue(String val) throws Exception{
        if (kind == ManifestFieldValueKind.String) {
            value = new String(val);
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.String.getValue(), kind.getValue()));
        }
    }
    public void setValue(Memo val) throws Exception{
        if (kind == ManifestFieldValueKind.Memo) {
            value = new Memo(val);
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.Memo.getValue(), kind.getValue()));
        }
    }
    public void setValue(VarString val) throws Exception{
        if (kind == ManifestFieldValueKind.StringList) {
            value = val.clone();
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.StringList.getValue(), kind.getValue()));
        }
    }

    public void setValue(int val) throws Exception{
        if (kind == ManifestFieldValueKind.Integer) {
            value = new Integer(val);
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.Integer.getValue(), kind.getValue()));
        }
    }
    public void setValue(long val) throws Exception{
        if (kind == ManifestFieldValueKind.Int64) {
            value = new Long(val);
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.Int64.getValue(), kind.getValue()));
        }
    }
    public void setValue(KeyItem val) throws Exception{
        if (kind == ManifestFieldValueKind.KeyPair) {
            value = new KeyItem(val) ;
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.KeyPair.getValue(), kind.getValue()));
        }
    }
    public void setValue(KeyPair val) throws Exception{
        if (kind == ManifestFieldValueKind.KeyPairList) {
            value = new KeyPair(val) ;
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.KeyPairList.getValue(), kind.getValue()));
        }
    }
    public void setValue(double val) throws Exception{
        if (kind == ManifestFieldValueKind.Double) {
            value = new Double(val) ;
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.Double.getValue(), kind.getValue()));
        }
    }
    public void setValue(boolean val) throws Exception{
        if (kind == ManifestFieldValueKind.Boolean) {
            value = new Boolean(val) ;
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.Boolean.getValue(), kind.getValue()));
        }
    }
}
