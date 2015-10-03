package com.aurawin.scs.stored.domain.vendor.hawker.item;

import com.aurawin.core.Memo;
import com.aurawin.core.array.KeyItem;
import com.aurawin.core.array.KeyPair;
import com.aurawin.core.array.VarString;
import com.aurawin.core.lang.Table;

public class FieldValue {

    private final FieldValueKind kind;
    private Object value;
    public FieldValueKind getKind(){ return kind;}
    public FieldValue(FieldValueKind aKind){
        kind = aKind;
    }
    public Object getData(){return value;}
    public void setValue(Object val){
        value = val;
    }
    public void setValue(String val) throws Exception{
        if (kind == FieldValueKind.String) {
            value = new String(val);
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.String.getValue(), kind.getValue()));
        }
    }
    public void setValue(Memo val) throws Exception{
        if (kind == FieldValueKind.Memo) {
            value = new Memo(val);
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.Memo.getValue(), kind.getValue()));
        }
    }
    public void setValue(VarString val) throws Exception{
        if (kind == FieldValueKind.StringList) {
            value = val.clone();
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.StringList.getValue(), kind.getValue()));
        }
    }

    public void setValue(int val) throws Exception{
        if (kind == FieldValueKind.Integer) {
            value = new Integer(val);
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.Integer.getValue(), kind.getValue()));
        }
    }
    public void setValue(long val) throws Exception{
        if (kind == FieldValueKind.Int64) {
            value = new Long(val);
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.Int64.getValue(), kind.getValue()));
        }
    }
    public void setValue(KeyItem val) throws Exception{
        if (kind == FieldValueKind.KeyPair) {
            value = new KeyItem(val) ;
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.KeyPair.getValue(), kind.getValue()));
        }
    }
    public void setValue(KeyPair val) throws Exception{
        if (kind == FieldValueKind.KeyPairList) {
            value = new KeyPair(val) ;
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.KeyPairList.getValue(), kind.getValue()));
        }
    }
    public void setValue(double val) throws Exception{
        if (kind == FieldValueKind.Double) {
            value = new Double(val) ;
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.Double.getValue(), kind.getValue()));
        }
    }
    public void setValue(boolean val) throws Exception{
        if (kind == FieldValueKind.Boolean) {
            value = new Boolean(val) ;
        } else {
            throw new Exception(Table.Format(Table.Exception.Entities.Vendor.Manifest.Field.ValueIsNotOfType, kind.Boolean.getValue(), kind.getValue()));
        }
    }
}
